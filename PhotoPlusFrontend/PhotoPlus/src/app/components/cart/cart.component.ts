import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart/cart.service';
import { LoginService } from 'src/app/services/login/login.service';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderItem } from 'src/app/models/orderItem/order-item';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  items: BehaviorSubject<OrderItem>[];
  price: number;

  constructor(private cartService: CartService, private loginService: LoginService, private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.items = this.cartService.getItems();
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
  }

  removeItem(item: OrderItem) {
    this.cartService.deleteFromCart(item);
  }

  onValueChange(value: number, item: OrderItem) {
    this.cartService.changeQuantity(value, item);
    (document.querySelector(('#input' + item.productCode).toString()) as HTMLInputElement).value = item.quantity.toString();
  }

  buy() {
    if (this.loginService.isLoggedIn() === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please login!';
      return;
    }
    // check if store quantity didn't change, update products
    this.cartService.updateCartAndBuy();
  }

}
