import { Component, OnInit, Input } from '@angular/core';
import { Product } from 'src/app/models/product/product';
import { BehaviorSubject } from 'rxjs';
import { CartService } from 'src/app/services/cart/cart.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { LoginService } from 'src/app/services/login/login.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  @Input() product: BehaviorSubject<Product>;

  constructor(private cartService: CartService,
              private modalService: NgbModal,
              private router: Router,
              private loginService: LoginService) { }

  ngOnInit(): void {
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = 'Please go to checkout to place an order.';
    modalRef.componentInstance.title = 'Added ' + product.name + ' to card.';
  }


  buy(product: Product) {
    if (!confirm('Are you sure you want to buy ' + product.name + '? \n This operation will clear your shopping cart.')) {
      return;
    }
    this.cartService.clearCart();
    this.cartService.addToCart(product);
    if (this.loginService.isLoggedIn() === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please login!';
      return;
    }
    this.router.navigate(['/order']);
  }
}
