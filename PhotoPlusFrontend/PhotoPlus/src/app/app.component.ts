import { Component, ViewChild} from '@angular/core';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subject, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs/operators';
import { LoginService } from './services/login/login.service';
import { ProductService } from './services/product/product.service';
import { Product } from './models/product/product';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private loginService: LoginService, private productService: ProductService) {

     this.productService.getAll().subscribe((data: Product[]) => {
        this.products = data;
        this.products.forEach(element => { this.productService.getDataFromLinks(element) });
      });
  }

  get auth(): LoginService {
    return this.loginService;
  }
  title = 'PhotoPlus';
  products: Product[];

    @ViewChild('instance', {static: true}) instance: NgbTypeahead;
    focus$ = new Subject<string>();
    click$ = new Subject<string>();

  logout() {
    this.loginService.logout();
    window.location.reload();
  }

    search = (text$: Observable<string>) => {
      const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());
      const clicksWithClosedPopup$ = this.click$.pipe(filter(() => !this.instance.isPopupOpen()));
      const inputFocus$ = this.focus$;

      return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
        map(term => (term === '' ? this.products.map(p => p.name)
          : this.products.map(p => p.name).filter(n => n.toLowerCase().indexOf(term.toLowerCase()) > -1)).slice(0, 10))
      );
    }
}
