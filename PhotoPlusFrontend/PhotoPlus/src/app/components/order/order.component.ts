import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from '../../models/product/product';
import { HttpClient, HttpResponse } from '@angular/common/http';

export interface Order {

  userCode: number;
  orderStatus: string;
  paymentMethod: string;
  price: number;
  orderItems:[{
    orderCode: string;
    productCode: number,
    quantity: number
  }]

}

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})


export class OrderComponent implements OnInit {
  items: [Product, number][];
  price: number;
  order: Order;
  products: Product[]
  user: any=[];
  arr: any=[]; 

  constructor(private cartService: CartService,private http: HttpClient) {
    this.cartService.getSummaryPrice().subscribe(value => this.price = value);
    this.order={

      userCode: 0,
      orderStatus: 'PENDING',
      paymentMethod: 'PAYPAL',
      price: 0,
      orderItems: [{productCode: 0,quantity:0, orderCode:'dupa'}]
    }
  }
  
  ngOnInit(): void {
    this.items = this.cartService.getItems();
    console.log(this.items)
    this.items.forEach(element => {
      this.order.orderItems.push({productCode: element[0].code,quantity:element[1],orderCode:'dupa'})
    });
    this.order.orderItems.splice(0,1)
    console.log(this.order)
    this.user=localStorage.getItem("loggedUser")
    this.user=JSON.parse(this.user)
    this.order.userCode=this.user.code
    this.order.price=this.price;
  }
  buy()
  {
    this.arr.push(this.order)
    console.log("dziala")
    console.log(this.arr)
    this.http.post<HttpResponse<Order[]>>('http://localhost:8090/order/buy', 
      
     this.arr

    ).subscribe(res => {
      console.log(res)
    })
  }
}
