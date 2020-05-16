import { Component, OnInit, Input } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { OrderService } from "src/app/services/order/order.service";
import { Order } from "src/app/models/order/order";
import { BehaviorSubject } from "rxjs";
import { User } from 'src/app/models/user/user';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: "app-manage-single-order",
  templateUrl: "./manage-single-order.component.html",
  styleUrls: ["./manage-single-order.component.scss"],
})
export class ManageSingleOrderComponent implements OnInit {

  order: BehaviorSubject<Order>;
  user: BehaviorSubject<User>;
//address: BehaviorSubject<Address>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private orderService: OrderService,
    private userService: UserService,
    // private addressService: AddressService // TODO wait for someone xD
  ) {}

  ngOnInit() {
    this.order = new BehaviorSubject({} as Order);
    this.user = new BehaviorSubject({} as User);
  //this.address = new BehaviorSubject({} as Address);  
    this.loadOrder();
  }

  async loadOrder() {
    this.activatedRoute.params.subscribe((params) => {
      this.orderService.getSingle(params["orderCode"]).subscribe((x) => {
        this.order.next(x);
        this.loadUser();
        this.loadAddress();
      });
    });
  }

  async loadUser(){
    this.user.next(await this.userService.getSingle(this.order.value.userCode).toPromise());
  }

  async loadAddress(){
  //const address = this.addressService.getSingle(this.order.value.addressCode).toPromise;
  //this.address.next(address);
  }
  
  beatutifyEnum(paymentMethod:string){
    if(!paymentMethod){
      return;
    }
    const noUnderscore = paymentMethod.replace(/\_/g, " ");
    return noUnderscore.charAt(0).toUpperCase() + noUnderscore.slice(1).toLowerCase();
  }

}
