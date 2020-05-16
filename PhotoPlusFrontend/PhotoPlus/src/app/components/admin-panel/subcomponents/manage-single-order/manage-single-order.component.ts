import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { OrderService } from "src/app/services/order/order.service";
import { Order } from "src/app/models/order/order";
import { BehaviorSubject } from "rxjs";
import { User } from 'src/app/models/user/user';
import { UserService } from 'src/app/services/user/user.service';
import { Address } from 'src/app/models/address/address';
import { AddressService } from 'src/app/services/address/address.service';
import { OrderItemService } from 'src/app/services/order-item/order-item.service';
import { OrderItem } from 'src/app/models/order-item/order-item';
import { retry } from 'rxjs/operators';

@Component({
  selector: "app-manage-single-order",
  templateUrl: "./manage-single-order.component.html",
  styleUrls: ["./manage-single-order.component.scss"],
})
export class ManageSingleOrderComponent implements OnInit {

  order: BehaviorSubject<Order>;
  user: BehaviorSubject<User>;
  address: BehaviorSubject<Address>;
  orderItems: BehaviorSubject<OrderItem>[];
  selectedOrderItemsPage: BehaviorSubject<number>;
  pageAmount: BehaviorSubject<number>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private orderService: OrderService,
    private userService: UserService,
    private addressService: AddressService,
    private orderItemService: OrderItemService
  ) {}

  ngOnInit() {
    this.order = new BehaviorSubject({} as Order);
    this.user = new BehaviorSubject({} as User);
    this.address = new BehaviorSubject({} as Address);
    this.selectedOrderItemsPage = new BehaviorSubject(0);
    this.loadOrder();
  }

  async loadOrder() {
    this.activatedRoute.params.subscribe((params) => {
      this.orderService.getSingle(params["orderCode"]).subscribe((x) => {
        this.order.next(x);
        this.loadUser();
        this.loadAddress();
        this.loadOrderItemsPageInfo();
      });
    });
  }

  async loadOrderItemsPageInfo(){
    const pageInfo = this.orderItemService.getPageInfoOfOrderItemsByOrder(this.order.value.code).toPromise();
    if((await pageInfo).pageAmount === 0){
      return;
    }
    this.pageAmount.next((await pageInfo).pageAmount);
    this.loadOrderItems();
  }

  async loadOrderItems(){
    if(this.order.value?.code){
      return;
    }
    const orderItems = this.orderItemService.getPageOfOrderItemsByOrder(this.selectedOrderItemsPage.value,this.order.value.code).toPromise();
    this.orderItems = new Array();
    for(const item of await orderItems){
      this.orderItems.push(new BehaviorSubject(item));
    }
    console.log(this.orderItems);
    
  }

  async loadUser(){
    this.user.next(await this.userService.getSingle(this.order.value.userCode).toPromise());
  }

  async loadAddress(){
    const address = await this.addressService.getSingle(this.order.value.addressCode).toPromise();
    this.address.next(address);
  }
  
  beatutifyEnum(paymentMethod:string){
    if(!paymentMethod){
      return;
    }
    const noUnderscore = paymentMethod.replace(/\_/g, " ");
    return noUnderscore.charAt(0).toUpperCase() + noUnderscore.slice(1).toLowerCase();
  }

  selectPage(pageNumber:number){
    console.log(pageNumber);
  }

}
