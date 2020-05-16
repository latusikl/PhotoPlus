import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order/order.service';
import { OrderStatus } from 'src/app/models/order-status/order-status';
import { Tuple } from 'src/app/helpers/tuple';
import { PageInfo } from 'src/app/models/page-info/pageInfo';

@Component({
  selector: 'app-manage-orders',
  templateUrl: './manage-multiple-orders.component.html',
  styleUrls: ['./manage-multiple-orders.component.scss']
})
export class ManageMultipleOrdersComponent implements OnInit {

  orderStatuses: string[] = [
    'Pending',
    'Paid',
    'Ready to ship',
    'Shipped',
    'Delivered'
  ]

  // Obiekt który ma jako klucze wartości enuma OrderStatus 
  // a wartości kluczy to wielkości stron poszczególnych OrderStatusów
  // klucze są opcjonalne żeby można było je dodawać pojedynczo 
  orderStatusPageInfo: {[key in OrderStatus]?:PageInfo };

  constructor(private orderService: OrderService) { }

  async ngOnInit() {
    this.orderStatusPageInfo = {};
    for(const orderStatus in OrderStatus){
      const response = this.orderService.getPageCountForOrdersOfStatus(orderStatus).toPromise();
      this.orderStatusPageInfo[orderStatus] = await response;
    }
    setTimeout(()=>{console.log(this.orderStatusPageInfo)},1000);
  }
}
