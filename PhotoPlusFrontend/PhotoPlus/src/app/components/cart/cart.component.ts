import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart/cart.service';
import { Product } from '../../models/product/product';
import { LoginService } from 'src/app/services/login/login.service';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  items: [Product, number][];
  price: number;

  constructor(private cartService: CartService, private loginService: LoginService, private modalService: NgbModal, private router: Router) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
  }

  ngOnInit(): void {
    this.items = this.cartService.getItems();
  }

  removeItem(item: [Product, number]) {
    this.cartService.deleteFromCart(item[0]);
  }

  onValueChange(value: number, item: [Product, number]) {
    if (value > 0 && value <= item[0].storeQuantity && Number.isInteger(+value)) {
      this.cartService.changeQuantity(value, item);
    } else {
      (document.querySelector(("#input" + item[0].code).toString()) as HTMLInputElement).value = item[1].toString();
    }
  }
  buy() {
    if (this.loginService.isLoggedIn() == false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = "Error occured!";
      modalRef.componentInstance.message = "Please login!.";
      return;
    }
    this.router.navigate(['/order']);
  }

}
