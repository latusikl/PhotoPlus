import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from '../../models/product/product';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Order } from 'src/app/models/order/order';
import { DatePipe } from '@angular/common';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'src/app/services/login/login.service';
import { Link } from 'src/app/models/link/link';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { analyzeAndValidateNgModules } from '@angular/compiler';


@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  providers: [DatePipe]
})


export class OrderComponent implements OnInit {
  items: [Product, number][];
  price: number;
  order: Order;
  user: any = [];
  paymentMethod: string;
  isChecked: boolean = false;
  error: any;
  myDate = new Date();
  addreses: any[];
  selectedOption: any;
  hostAddress = environment.hostAddress;
  address: any;
  street: any;
  number: any;
  city: any;
  zipCode: any;
  country: any;
  addresLink: any;
  getAddress: any;


  constructor(private cartService: CartService, private http: HttpClient, private datePipe: DatePipe, private modalService: NgbModal, private loginService: LoginService) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);

    this.order = {
      userCode: 0,
      orderStatus: 'PENDING',
      paymentMethod: 'PAYPAL',
      price: 0,
      date: "2020-01-30",
      addressCode: 0,
      orderItems: [{ productCode: 0, quantity: 0, orderCode: 'orderCode' }],
      address: {
        street: "street",
        number: 0,
        zipCode: "00000",
        city: "city",
        countryCode: "code",
        userCode: 0,

      }
    }
  }





  selectOption(id: any[]) {
    this.selectedOption = id;
    console.log(this.selectedOption)
    this.order.address.city = this.addreses[this.selectedOption].city
    this.order.address.countryCode = this.addreses[this.selectedOption].country
    this.order.address.number = this.addreses[this.selectedOption].number
    this.order.address.street = this.addreses[this.selectedOption].street
    this.order.address.userCode = this.addreses[this.selectedOption].userCode
    this.order.address.zipCode = this.addreses[this.selectedOption].zipCode
    this.order.addressCode = this.addreses[this.selectedOption].code
  }

  FieldsChange(values: any) {
    this.isChecked = true;
    this.paymentMethod = values.target.value
    console.log(this.paymentMethod)
  }


  ngOnInit(): void {


    this.user = localStorage.getItem("loggedUser")
    this.user = JSON.parse(this.user)
    if (this.loginService.isLoggedIn() == true) {
      this.order.userCode = this.user.code
    }
    console.log(environment.hostAddress + "address/byUser/" + this.user.code)
    if (this.loginService.isLoggedIn() == true) {
      this.http.get<HttpResponse<[]>>(environment.hostAddress + "address/byUser/" + this.user.code,
      ).subscribe((data: any) => {
        console.log(data)
        let i = 0;
        data.forEach(element => {
          element.key = i;
          i++
        });
        this.addreses = data
        console.log("Adres")
        console.log(this.addreses)
      })
    }

    this.items = this.cartService.getItems();
    this.items.forEach(element => {
      this.order.orderItems.push({ productCode: element[0].code, quantity: element[1], orderCode: 'orderCode' })
    });
    this.order.orderItems.splice(0, 1)
    this.order.price = this.price;
    this.order.date = this.datePipe.transform(this.myDate, 'yyyy-MM-dd');
  }



  addresBuy() {

    this.street = document.getElementById("street");
    this.number = document.getElementById("number");
    this.city = document.getElementById("city");
    this.zipCode = document.getElementById("zipCode");
    this.country = document.getElementById("country");
    this.order.address.street = this.street.value;
    this.order.address.number = this.number.value;
    this.order.address.city = this.city.value;
    this.order.address.zipCode = this.zipCode.value;
    this.order.address.userCode = this.user.code
    this.order.address.countryCode = this.country.value;
    if (this.isChecked == false) {
      this.error = document.getElementById("error")
      this.error.style.display = "block"
      return
    }

    if (this.isChecked == true) {
      this.error = document.getElementById("error")
      this.error.style.display = "none"
    }
    this.http.post<HttpResponse<any>>(environment.hostAddress + 'address',
      {
        "code": 0,
        "street": this.order.address.street,
        "number": this.order.address.number,
        "zipCode": this.order.address.zipCode,
        "city": this.order.address.city,
        "countryCode": this.order.address.countryCode,
        "userCode": this.order.userCode
      }, { observe: 'response' }).subscribe(res => {
        this.addresLink = res.headers.get('location');
        this.http.get<HttpResponse<any>>(this.addresLink).subscribe(data => {
          this.getAddress = data;
          this.order.addressCode = this.getAddress.code
          this.http.post<HttpResponse<Order[]>>(environment.hostAddress + 'order/buy',
            this.order
          ).subscribe()
        })
      })


    this.order.paymentMethod = this.paymentMethod

    console.log(this.order)

  }

  buy() {


    if (this.loginService.isLoggedIn() == false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = "Error occured!";
      modalRef.componentInstance.message = "Please login!.";
      return;
    }

    if (this.isChecked == false) {
      this.error = document.getElementById("error")
      this.error.style.display = "block"
      return
    }

    if (this.isChecked == true) {
      this.error = document.getElementById("error")
      this.error.style.display = "none"
    }

    this.order.paymentMethod = this.paymentMethod
    console.log(this.order)

    this.http.post<HttpResponse<Order[]>>(environment.hostAddress + 'order/buy',
      this.order
    ).subscribe()

  }

}
