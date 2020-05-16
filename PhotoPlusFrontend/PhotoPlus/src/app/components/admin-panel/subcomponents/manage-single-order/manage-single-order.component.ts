import { Component, OnInit, Input } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { OrderService } from "src/app/services/order/order.service";
import { Order } from "src/app/models/order/order";
import { BehaviorSubject } from "rxjs";

@Component({
  selector: "app-manage-single-order",
  templateUrl: "./manage-single-order.component.html",
  styleUrls: ["./manage-single-order.component.scss"],
})
export class ManageSingleOrderComponent implements OnInit {

  order: BehaviorSubject<Order>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit() {
    this.order = new BehaviorSubject({} as Order);
    this.loadOrder();
  }

  async loadOrder() {
    this.activatedRoute.params.subscribe((params) => {
      this.orderService.getSingle(params["orderCode"]).subscribe((x) => {
        this.order.next(x);
      });
    });
  }
}
