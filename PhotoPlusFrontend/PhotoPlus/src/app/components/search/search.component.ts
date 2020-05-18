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

  searchedText: string;
  products: BehaviorSubject<Product>[];

  constructor(private productService: ProductService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.loadSearchedText();
    this.loadProducts();
  }

  loadSearchedText() {
    this.route.params.subscribe(params => {
      this.searchedText = params.searchedText;
    });
  }

  loadProducts() {
    this.products = new Array<BehaviorSubject<Product>>();
    this.productService.getProductsSearchByName(this.searchedText).subscribe(data => {
      console.log(data);
      for (const product of data) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }
}
