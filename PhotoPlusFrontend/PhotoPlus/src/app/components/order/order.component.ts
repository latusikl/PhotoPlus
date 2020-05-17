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
import { OrderItem } from 'src/app/models/orderItem/order-item';
import { Address } from 'src/app/models/address/address';


@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  providers: [DatePipe]
})


export class OrderComponent implements OnInit {
  items: OrderItem[];
  price: number;
  order: Order;
  myDate = new Date();
  addresses: Address[] = new Array();
  addressForm: FormGroup;
  paymentMethodForm: FormGroup;
  submitted: boolean = false;
  paymentMethodSubmitted = false;

  constructor(private formBuilder: FormBuilder, private router: Router, private addressService: AddressService, private cartService: CartService, private orderSerivce: OrderService, private http: HttpClient, private datePipe: DatePipe, private modalService: NgbModal, private loginService: LoginService) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
    this.order = new Order();
    this.order.orderStatus = "PENDING";
    this.order.orderItems = new Array();
    this.order.address = new Address();
  }

  selectOption(addressCode: string) {
    let chosenAddress = this.addresses.find(el => el.code == addressCode);
    this.order.address = chosenAddress;
    this.order.addressCode = addressCode;
    console.log("chose option")
  }

  ngOnInit(): void {
    this.addressForm = this.formBuilder.group({
      street: ['', [Validators.required, Validators.minLength(4)]],
      number: ['', [Validators.required]],
      city: ['', [Validators.required]],
      zipCode: ['', [Validators.required, Validators.pattern(/\d{2,5}-?\d{2,5}/)]],
      country: ['PL', [Validators.required]],
    })

    this.paymentMethodForm = this.formBuilder.group({
      paymentMethod: ['CARD', Validators.required]
    });

    if (this.loginService.isLoggedIn() == true) {
      this.order.userCode = this.loginService.getLoggedUser().code
      this.addressService.byUser(this.order.userCode).subscribe(data => {
        this.addresses = data.reverse();
        this.selectOption(this.addresses[0].code);
        console.log(this.addresses)
      })
    }
    this.items = this.cartService.getItems();
    this.items.forEach(element => {
      this.order.orderItems.push(element);
    });
    this.order.orderItems.splice(0, 1)
    this.order.price = this.price;
    this.order.date = this.datePipe.transform(this.myDate, 'yyyy-MM-dd');
  }

  get f() { return this.addressForm.controls; }

  addressBuy() {
    console.log(this.addresses)
    this.submitted = true;
    if (this.addressForm.invalid) {
      return;
    }
    console.log(this.addressForm.value);
    console.log(this.addressForm.invalid);
    

    this.paymentMethodSubmitted = true;
    if (this.paymentMethodForm.invalid) {
      return;
    }

    if (this.loginService.isLoggedIn() == false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = "Error occured!";
      modalRef.componentInstance.message = "Please login!";
      return;
    }

    const alias = this.addressForm.value;
    // this.order.address.street = alias.street;
    // this.order.address.number = alias.number;
    // this.order.address.city = alias.city;
    // this.order.address.zipCode = alias.zipCode;
    // this.order.address.countryCode = alias.country;
    // this.order.paymentMethod = this.paymentMethodForm.value.paymentMethod;
    // this.order.address.userCode = this.loginService.getLoggedUser().code;
    const address: Address = {
      code: null,
      city: alias.city,
      countryCode: alias.countryCode,
      links: null,
      number: alias.number,
      street: alias.street,
      userCode: this.loginService.getLoggedUser().code,
      zipCode: alias.zipCode
    }
    this.addressService.post(address)
      .subscribe(res => {
        this.addressService.getSingle(res.headers.get('location').substring(30)).subscribe(data => {
          console.log(data.code)
          this.order.addressCode = data.code;
          this.orderSerivce.buy(this.order).subscribe(data => {
            const modalRef = this.modalService.open(SuccessModalComponent);
            modalRef.componentInstance.title = "Success!";
            modalRef.componentInstance.message = "Your order is being carried.";
            this.cartService.clearCart();
            this.router.navigate(['/home']);
          }, error => {
            this.router.navigate(['/cart']);
          })
        })
      })
  }

  buy() {
    this.paymentMethodSubmitted = true;
    if (this.paymentMethodForm.invalid) {
      return;
    }

    if (this.loginService.isLoggedIn() == false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = "Error occured!";
      modalRef.componentInstance.message = "Please login!";
      return;
    }

    this.order.paymentMethod = this.paymentMethodForm.value.paymentMethod;
    this.orderSerivce.buy(this.order).subscribe(data => {
      const modalRef = this.modalService.open(SuccessModalComponent);
      modalRef.componentInstance.title = "Success!";
      modalRef.componentInstance.message = "Your order is being carried.";
      this.cartService.clearCart();
      this.router.navigate(['/products']);
    });
  }

}
