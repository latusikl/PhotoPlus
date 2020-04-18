import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart/cart.service';
import { Product } from '../../models/product/product';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  items: [Product, number][];
  price: number;

  constructor(private cartService: CartService,) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
  }

  ngOnInit(): void {
    this.items = this.cartService.getItems();
  }

  removeItem(item: [Product, number]) {
    this.cartService.deleteFromCart(item[0]);
  }

  onValueChange(value: number, item: [Product, number]) {
    if (value > 0 && value < 100 && Number.isInteger(+value)) {
      this.cartService.changeQuantity(value, item);
    } else {
      (document.querySelector("#quantityInput") as HTMLInputElement).value = item[1].toString();
    }
  }

}
