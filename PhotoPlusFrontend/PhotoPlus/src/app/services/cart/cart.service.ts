import { Injectable } from '@angular/core';
import { Product } from 'src/app/models/product/product';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  items: Product[] = new Array<Product>();

  constructor() { }

  addToCart(product: Product) {
    this.items.push(product);
  }

  getItems() {
    return this.items;
  }

  clearCart() {
    this.items = new Array<Product>();
  }


}
