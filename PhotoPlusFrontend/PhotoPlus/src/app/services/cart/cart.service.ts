import { Injectable } from '@angular/core';
import { Product } from 'src/app/models/product/product';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  //items with quantity
  items: [Product, number][];

  constructor() {
    this.items = localStorage.getItem('items') ? JSON.parse(localStorage.getItem('items')) : [];
  }

  addToCart(product: Product) {
    const index = this.items.findIndex(it => it[0].code == product.code);
    if (index > -1) {
      this.items[index][1]++;
    } else {
      this.items.push([product, 1]);
    }
    this.save();
  }

  deleteFromCart(product: Product) {
    const index = this.items.findIndex(it => it[0].code == product.code);
    if (index > -1) {
      this.items.splice(index, 1);
    }
    this.save();
  }

  getItems() {
    return this.items;
  }

  clearCart() {
    this.items = new Array<[Product, number]>();
    this.save();
  }

  save() {
    localStorage.setItem('items', JSON.stringify(this.items));
  }


}
