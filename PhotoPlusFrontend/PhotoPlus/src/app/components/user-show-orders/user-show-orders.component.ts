import { Component, OnInit } from '@angular/core';
import { OrderService } from "../../services/order/order.service";
import { LoginService } from "../../services/login/login.service";

@Component({
  selector: 'app-user-show-orders',
  templateUrl: './user-show-orders.component.html',
  styleUrls: ['./user-show-orders.component.scss']
})
export class UserShowOrdersComponent implements OnInit {

  constructor(private orderService: OrderService, private loginService: LoginService) { }

  orderCodes: string[];

  ngOnInit(): void {
    this.orderService.getOrderCodesByUser(this.loginService.getLoggedUserCode()).subscribe((res: string[]) => this.orderCodes = res);
  }

}
