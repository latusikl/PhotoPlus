import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart/cart.service';
import { HttpClient } from '@angular/common/http';
import { Order } from 'src/app/models/order/order';
import { DatePipe } from '@angular/common';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'src/app/services/login/login.service';
import { OrderService } from 'src/app/services/order/order.service';
import { AddressService } from 'src/app/services/address/address.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Address } from 'src/app/models/address/address';
import { OrderItem } from 'src/app/models/orderItem/order-item';
import { BehaviorSubject } from 'rxjs';


@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  providers: [DatePipe]
})


export class OrderComponent implements OnInit {
  price: number;
  order: Order;
  myDate = new Date();
  addresses: Address[];
  addressForm: FormGroup;
  paymentMethodForm: FormGroup;
  submitted = false;
  paymentMethodSubmitted = false;
  items: BehaviorSubject<OrderItem>[];

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private addressService: AddressService,
              private cartService: CartService,
              private orderSerivce: OrderService,
              private http: HttpClient,
              private datePipe: DatePipe,
              private modalService: NgbModal,
              private loginService: LoginService) {}

  selectOption(addressCode: string) {
    const chosenAddress = this.addresses.find(el => el.code === addressCode);
    this.order.address = chosenAddress;
    this.order.addressCode = addressCode;
  }

  ngOnInit(): void {
    this.items = this.cartService.getItems();
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
    this.order = new Order();
    this.order.orderStatus = 'PENDING';
    this.order.address = new Address();
    this.addressForm = this.formBuilder.group({
      street: ['', [Validators.required, Validators.minLength(4)]],
      number: ['', [Validators.required]],
      city: ['', [Validators.required]],
      zipCode: ['', [Validators.required, Validators.pattern(/\d{2,5}-?\d{2,5}/)]],
      country: ['PL', [Validators.required]],
    });

    this.paymentMethodForm = this.formBuilder.group({
      paymentMethod: ['CARD', Validators.required]
    });

    if (this.loginService.isLoggedIn() === true) {
      this.order.userCode = this.loginService.getLoggedUser().code;
      this.addressService.byUser(this.order.userCode).subscribe(data => {
        if(data.length === 0){
          return;
        }
        this.addresses = data.reverse();
        this.selectOption(this.addresses[0].code);
      });
    }
    this.order.price = this.price;
    this.order.date = this.datePipe.transform(this.myDate, 'yyyy-MM-dd');
  }

  get f() { return this.addressForm.controls; }

  addressBuy() {
    this.submitted = true;
    if (this.addressForm.invalid) {
      return;
    }

    this.paymentMethodSubmitted = true;
    if (this.paymentMethodForm.invalid) {
      return;
    }

    if (this.loginService.isLoggedIn() === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please login!';
      return;
    }

    const alias = this.addressForm.value;
    const address: Address = {
      code: null,
      city: alias.city,
      country: alias.country,
      links: null,
      number: alias.number,
      street: alias.street,
      userCode: this.loginService.getLoggedUser().code,
      zipCode: alias.zipCode,
    };

    this.addressService.post(address)
      .subscribe(res => {
        const sliced = res.headers.get('location').split("/");
        const lastItem = sliced[sliced.length-1];

        this.order.addressCode = lastItem;
        this.order.paymentMethod = this.paymentMethodForm.value.paymentMethod;
        this.order.orderItems = this.cartService.getItemsModel();
        this.orderSerivce.buy(this.order).subscribe(() => {
          const modalRef = this.modalService.open(SuccessModalComponent);
          modalRef.componentInstance.title = 'Success!';
          modalRef.componentInstance.message = 'Your order is being carried.';
          this.cartService.clearCart();
          this.router.navigate(['/']);
        }, error => {
          this.router.navigate(['/cart']);
        });
      });
  }

  buy() {
    this.paymentMethodSubmitted = true;
    if (this.paymentMethodForm.invalid) {
      return;
    }

    if (this.loginService.isLoggedIn() === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please login!';
      return;
    }

    this.order.orderItems = this.cartService.getItemsModel();
    this.order.paymentMethod = this.paymentMethodForm.value.paymentMethod;
    this.orderSerivce.buy(this.order).subscribe(data => {
      const modalRef = this.modalService.open(SuccessModalComponent);
      modalRef.componentInstance.title = 'Success!';
      modalRef.componentInstance.message = 'Your order is being carried.';
      this.cartService.clearCart();
      this.router.navigate(['/products']);
    });
  }

}
