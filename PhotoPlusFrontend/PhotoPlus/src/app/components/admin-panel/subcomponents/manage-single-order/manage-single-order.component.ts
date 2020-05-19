import { Component, OnInit, ViewChild, ElementRef, Input } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { OrderService } from "src/app/services/order/order.service";
import { Order } from "src/app/models/order/order";
import { BehaviorSubject } from "rxjs";
import { User } from "src/app/models/user/user";
import { UserService } from "src/app/services/user/user.service";
import { Address } from "src/app/models/address/address";
import { AddressService } from "src/app/services/address/address.service";
import { OrderItemService } from "src/app/services/order-item/order-item.service";
import { OrderItem } from "src/app/models/order-item/order-item";
import { Tuple } from "src/app/helpers/tuple";
import { Product } from "src/app/models/product/product";
import { ProductService } from "src/app/services/product/product.service";
import { OrderStatus } from "src/app/models/order-status/order-status";
import { PaymentMethod } from "src/app/models/payment-method/payment-method";

@Component({
  selector: "app-manage-single-order",
  templateUrl: "./manage-single-order.component.html",
  styleUrls: ["./manage-single-order.component.scss"],
})
export class ManageSingleOrderComponent implements OnInit {
  @ViewChild("selectedStatus")
  selectedStatus: ElementRef;
  @Input("isForClient")
  isForClient: false;
  @Input("orderCode")
  orderCode: string;

  order: BehaviorSubject<Order>;
  user: BehaviorSubject<User>;
  address: BehaviorSubject<Address>;
  orderedProducts: BehaviorSubject<Tuple<OrderItem, Product>>[];
  selectedOrderItemsPage: BehaviorSubject<number>;
  pageAmount: BehaviorSubject<number>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private orderService: OrderService,
    private userService: UserService,
    private addressService: AddressService,
    private orderItemService: OrderItemService,
    private productService: ProductService
  ) { }

  ngOnInit() {
    this.order = new BehaviorSubject({} as Order);
    this.user = new BehaviorSubject({} as User);
    this.address = new BehaviorSubject({} as Address);
    this.orderedProducts = new Array();
    this.selectedOrderItemsPage = new BehaviorSubject(0);
    this.pageAmount = new BehaviorSubject(0);
    this.loadOrder();
  }

  async loadOrder() {
    if (this.isForClient) {
      this.orderService.getOrderDetailsByUser(this.orderCode).subscribe((x) => {
        this.order.next(x);
        this.loadAddressByUser();
        this.loadOrderItemsPageInfo();
      });
    }
    else {
      this.activatedRoute.params.subscribe((params) => {
        this.orderService.getSingle(params["orderCode"]).subscribe((x) => {
          this.order.next(x);
          this.loadUser();
          this.loadAddress();
          this.loadOrderItemsPageInfo();
        });
      });
    }
  }

  async loadOrderItemsPageInfo() {
    const pageInfo = this.orderItemService
      .getPageInfoOfOrderItemsByOrder(this.order.value.code)
      .toPromise();
    if ((await pageInfo).pageAmount === 0) {
      return;
    }
    this.pageAmount.next((await pageInfo).pageAmount);
    this.loadOrderItems();
  }

  async loadOrderItems() {
    if (!this.order.value?.code) {
      return;
    }
    const orderItems = this.orderItemService
      .getPageOfOrderItemsByOrder(
        this.selectedOrderItemsPage.value,
        this.order.value.code
      )
      .toPromise();
    this.orderedProducts = new Array();
    for (const item of await orderItems) {
      const product = await this.productService
        .getSingle(item.productCode)
        .toPromise();
      this.productService.getDataFromLinks(product);
      this.orderedProducts.push(
        new BehaviorSubject({
          first: item,
          second: product,
        })
      );
    }
  }

  async loadUser() {
    this.user.next(
      await this.userService.getSingle(this.order.value.userCode).toPromise()
    );
  }

  async loadAddress() {
    const address = await this.addressService
      .getSingle(this.order.value.addressCode)
      .toPromise();
    this.address.next(address);
  }

  async loadAddressByUser() {
    const address = await this.addressService
      .getSingleByUser(this.order.value.addressCode)
      .toPromise();
    this.address.next(address);
  }

  beatutifyEnum(paymentMethod: string) {
    if (!paymentMethod) {
      return;
    }
    const noUnderscore = paymentMethod.replace(/\_/g, " ");
    return (
      noUnderscore.charAt(0).toUpperCase() + noUnderscore.slice(1).toLowerCase()
    );
  }

  goToImage(imageCode: string) {
    if (!imageCode) {
      return;
    }
    this.router.navigate(["imageDisplay", imageCode]);
  }

  selectPage(pageNumber: number) {

  }

  async updateOrderStatus() {
    const status = this.selectedStatus.nativeElement.value;
    const orderResponse = await this.orderService.patch(this.order.value.code, {
      orderStatus: status,
    } as Order);
    orderResponse.subscribe(() => {
      this.loadOrder();
      alert("Order status changed.");
    });
  }

  orderStatuses() {
    let orderStatuses = Object.keys(OrderStatus);
    if (
      this.order.value.paymentMethod?.toLowerCase() ===
      PaymentMethod.CASH_ON_DELIVERY?.toLowerCase()
    ) {
      orderStatuses = orderStatuses.filter(
        (x) => x.toLowerCase() !== OrderStatus.PAID.toLowerCase()
      );
    }
    return orderStatuses;
  }
}
