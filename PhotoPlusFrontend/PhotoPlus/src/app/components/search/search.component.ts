import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from 'src/app/models/product/product';
import { ProductService } from 'src/app/services/product/product.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

  sortBy = 'name';

  searchedText: string;
  products: BehaviorSubject<Product>[];

  selectedPage: BehaviorSubject<number>;
  amountOfPages: BehaviorSubject<number>;

  constructor(private productService: ProductService, private route: ActivatedRoute) { }

  async ngOnInit(): Promise<void> {
    this.selectedPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    const pageInfo = this.productService.getPageCount().toPromise();
    this.loadSearchedText();
    this.loadProducts();
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  loadSearchedText() {
    this.route.params.subscribe(params => {
      this.searchedText = params.searchedText;
    });
  }

  loadProducts() {
    this.products = new Array<BehaviorSubject<Product>>();
    if (this.searchedText.length > 2) {
      this.productService.getProductsSearchByName(this.selectedPage.value, this.sortBy, this.searchedText).subscribe(data => {
        for (const product of data) {
          this.productService.getDataFromLinks(product);
          this.products.push(new BehaviorSubject(product));
        }
      });
    }
  }

  changePage(page: number) {
    this.selectedPage.next(page);
    this.loadProducts();
  }

  onSortingChange() {
    this.loadProducts();
  }

}
