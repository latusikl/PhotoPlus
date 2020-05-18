import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order/order.service';
import { OrderStatus } from 'src/app/models/order-status/order-status';
import { PageInfo } from 'src/app/models/page-info/page-info';
import { Order } from 'src/app/models/order/order';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manage-orders',
  templateUrl: './manage-multiple-orders.component.html',
  styleUrls: ['./manage-multiple-orders.component.scss']
})
export class ManageMultipleOrdersComponent implements OnInit {

  // Obiekt który ma jako klucze wartości enuma OrderStatus 
  // a wartości kluczy to wielkości stron poszczególnych OrderStatusów
  // klucze są opcjonalne aby wartości można było dodawać pojedynczo 
  orderStatusAmountPages: {[key in OrderStatus]?: BehaviorSubject<number> };
  selectedPagesNumbers: {[key in OrderStatus]?: BehaviorSubject<number>}
  currentStatusesOrders: {[key in OrderStatus]?: BehaviorSubject<Order[]>}


  constructor(private orderService: OrderService, private router: Router) { }

  async ngOnInit() {
    this.loadOrderStatusPagesInfo();
    this.setDefaultPageNumbers();
    this.loadSelectedPages();
  }

  async loadOrderStatusPagesInfo(){
    this.orderStatusAmountPages = {};
    for(const orderStatus in OrderStatus){
      const response = this.orderService.getPageCountForOrdersOfStatus(orderStatus).toPromise();
      this.orderStatusAmountPages[orderStatus] = new BehaviorSubject((await response).pageAmount);
    }
  }

  setDefaultPageNumbers(){
    this.selectedPagesNumbers = {};
    for(const status in OrderStatus){
      this.selectedPagesNumbers[status] = 0;
    }
  }

  async loadSelectedPages(){
    this.currentStatusesOrders = {};
    for(const status in OrderStatus){
      const pageNumer = this.selectedPagesNumbers[status];
      let statusPage = this.orderService.getPageByOrderStatus(pageNumer, status).toPromise();
      this.currentStatusesOrders[status] = new BehaviorSubject(await statusPage);      
    }
  }

  goToSingleOrder(orderCode:string){
    this.router.navigate(["manage/orders", orderCode]);
  }

  humanReadable(text: string): string{
    const noUnderscores = text.replace(/\_/g, " ")
    return noUnderscores.charAt(0) + noUnderscores.slice(1).toLowerCase();
  }

  toCssClass(orderStatus: string): string{
    return orderStatus.toLowerCase() + '_bg_color';
  }

  async changePage(orderStatus: OrderStatus, page: number){
    const nextPage = this.orderService.getPageByOrderStatus(page, orderStatus).toPromise();
    this.currentStatusesOrders[orderStatus].next(await nextPage);
  }

  get orderStatuses(): Array<string>{
    return Object.keys(this.orderStatusAmountPages);
  }
}
