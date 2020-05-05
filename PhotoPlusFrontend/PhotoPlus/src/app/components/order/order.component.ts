import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from '../../models/product/product';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})


export class OrderComponent implements OnInit {
  items: [Product, number][];
  price: number;
  constructor(private cartService: CartService) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
  }
  ngOnInit(): void {
    this.items = this.cartService.getItems();
  }

}
