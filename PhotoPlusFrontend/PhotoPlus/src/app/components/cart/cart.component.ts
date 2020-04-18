import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart/cart.service';
import { Product } from '../../models/product/product';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  items: [Product, number][];
  price: number;

  constructor(private cartService: CartService ) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
  }

  ngOnInit(): void {
    this.items = this.cartService.getItems();
  }

  removeItem(item: [Product, number]) {
    this.cartService.deleteFromCart(item[0]);
  }

  onValueChange(value: number, item: [Product, number]) {
    this.cartService.changeQuantity(value, item);
  }

}
