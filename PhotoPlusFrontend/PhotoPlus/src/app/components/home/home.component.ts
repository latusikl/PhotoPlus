import { Component, OnInit } from '@angular/core';
import { ProductService } from "../../services/product/product.service"
import { Product } from '../../models/product/product';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  products: Product[];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.productService.getProducts().subscribe((data: any) => {
    this.products = data;
    this.products.forEach(element => { this.productService.getDataFromLinks(element) });
    });
  }
}
