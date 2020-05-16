import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order/order.service';
import { OrderStatus } from 'src/app/models/order-status/order-status';
import { PageInfo } from 'src/app/models/page-info/page-info';
import { Order } from 'src/app/models/order/order';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-manage-orders',
  templateUrl: './manage-multiple-orders.component.html',
  styleUrls: ['./manage-multiple-orders.component.scss']
})
export class ManageMultipleOrdersComponent implements OnInit {

  // Obiekt który ma jako klucze wartości enuma OrderStatus 
  // a wartości kluczy to wielkości stron poszczególnych OrderStatusów
  // klucze są opcjonalne aby wartości można było dodawać pojedynczo 
  orderStatusPageInfo: {[key in OrderStatus]?:PageInfo };
  selectedPagesNumbers: {[key in OrderStatus]?: number}
  currentStatusesOrders: {[key in OrderStatus]?: BehaviorSubject<Order[]>}


  constructor(private orderService: OrderService) { }

  async ngOnInit() {
    this.loadOrderStatusPagesInfo();
    this.setDefaultPageNumbers();
    this.loadSelectedPages();
  }

  async loadOrderStatusPagesInfo(){
    this.orderStatusPageInfo = {};
    for(const orderStatus in OrderStatus){
      const response = this.orderService.getPageCountForOrdersOfStatus(orderStatus).toPromise();
      this.orderStatusPageInfo[orderStatus] = await response;
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
    console.log(orderCode);
    
  }

  humanReadable(text: string): string{
    const noUnderscores = text.replace(/\_/g, " ")
    return noUnderscores.charAt(0) + noUnderscores.slice(1).toLowerCase();
  }

  toCssClass(orderStatus: string): string{
    return orderStatus.toLowerCase() + '_bg_color';
  }

  changePage(orderStatus: OrderStatus, page: string){
    console.log("change page", orderStatus, page);
  }

  get orderStatuses(): Array<string>{
    return Object.keys(this.orderStatusPageInfo);
  }
}
