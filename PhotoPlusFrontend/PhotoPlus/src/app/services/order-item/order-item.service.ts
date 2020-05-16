import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { OrderItem } from 'src/app/models/order-item/order-item';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageInfo } from 'src/app/models/page-info/page-info';

@Injectable({
  providedIn: 'root'
})
export class OrderItemService extends AbstractService<OrderItem> {

  constructor(http: HttpClient) {
    super(http, "orderItem");
  }

  getPageOfOrderItemsByOrder(pageNubmer: number, orderCode: string): Observable<OrderItem[]>{
    return this._http.get<OrderItem[]>(this.hostAddress + this.endpointUrl + "/" + pageNubmer + "?orderCode=" + orderCode);
  }

  getPageInfoOfOrderItemsByOrder(orderCode: string): Observable<PageInfo>{
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + "/page/count?orderCode=" + orderCode);
  }

}
