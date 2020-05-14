import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ProductService } from "../../services/product/product.service";
import { Product } from 'src/app/models/product/product';
import { CartService } from 'src/app/services/cart/cart.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { LoginService } from 'src/app/services/login/login.service';



@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  param: string;
  product: Product;

  constructor(private route: ActivatedRoute, private productService: ProductService, private cartService: CartService, private modalService: NgbModal, private router: Router, private loginService: LoginService) { }

  ngOnInit(): void {
    this.route.paramMap.forEach(({ params }: Params) => {
      this.param = params['productCode']
    })
    this.productService.getSingle(this.param).subscribe((data: Product) => {
      this.product = data;
      this.productService.getDataFromLinks(this.product);
    });
  }

  buy(product: Product) {
    this.cartService.clearCart();
    this.cartService.addToCart(product);
    if (this.loginService.isLoggedIn() == false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = "Error occured!";
      modalRef.componentInstance.message = "Please login!.";
      return;
    }
    this.router.navigate(['/order']);
  }

  addToCart(product: Product) {
    this.cartService.addToCart(this.product);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = 'Please go to checkout to place an order.';
    modalRef.componentInstance.title = 'Added ' + product.name + ' to card.';
  }
}
