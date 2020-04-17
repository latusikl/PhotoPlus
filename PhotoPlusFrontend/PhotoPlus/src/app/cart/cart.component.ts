import { Component, OnInit } from '@angular/core';
import { CartService } from '../services/cart/cart.service';
import { Product } from '../models/product/product';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  items: [Product, number][];

  constructor(private cartService: CartService ) {}

  ngOnInit(): void {
    this.items = this.cartService.getItems();
  }

  removeItem(item: [Product, number]) {
    this.cartService.deleteFromCart(item[0]);
  }

}
