import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product/product';
import { BehaviorSubject } from 'rxjs';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  sortBy = 'name';

  products: BehaviorSubject<Product>[];

  selectedPage: BehaviorSubject<number>;
  amountOfPages: BehaviorSubject<number>;


  constructor(private productService: ProductService) { }

  async ngOnInit() {
    this.selectedPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    const pageInfo = this.productService.getSortedPageInfo().toPromise();
    this.loadProducts();
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  loadProducts() {
    this.products = new Array<BehaviorSubject<Product>>();
    this.productService.getSortedPage(this.selectedPage.value, this.sortBy).subscribe((data) => {
      for (const product of data) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }

  changePage(page: number) {
    this.selectedPage.next(page);
    this.loadProducts();
  }

  onSortingChange() {
    this.loadProducts();
  }

}
