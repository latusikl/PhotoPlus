import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product/product';
import { environment } from '../../../environments/environment';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  products: BehaviorSubject<Product>[];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.products = new Array<BehaviorSubject<Product>>();
    this.productService.getAllFromLink(environment.hostAddress + 'product/top').subscribe(data => {
      for (const product of data) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }
}
