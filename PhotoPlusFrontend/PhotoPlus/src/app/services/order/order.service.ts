import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Order } from 'src/app/models/order/order';
import { HttpClient } from '@angular/common/http';
import { OrderStatus } from 'src/app/models/order-status/order-status';
import { PageInfo } from 'src/app/models/page-info/page-info';
import { Observable } from 'rxjs';
import { LoginService } from "../login/login.service";

@Injectable({
  providedIn: 'root'
})
export class OrderService extends AbstractService<Order> {

  constructor(http: HttpClient) {
    super(http, "order");
  }

  postOrder(order: Order) {
    return this._http.post(this.hostAddress + this.endpointUrl + "/buy", order);
  }

  getPageCountForOrdersOfStatus(status: OrderStatus | string): Observable<PageInfo> {
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + "/page/count?orderStatus=" + status);
  }

  getPageByOrderStatus(pageNumber: number, status: OrderStatus | string): Observable<Order[]> {
    return this._http.get<Order[]>(this.hostAddress + this.endpointUrl + "/" + pageNumber + "?orderStatus=" + status);
  }

  getOrderCodesByUser(userCode: string): Observable<string[]> {
    return this._http.get<string[]>(this.hostAddress + this.endpointUrl + "/byUser/" + userCode);
  }

  getOrderDetailsByUser(orderCode: string) {
    return this._http.get<Order>(this.hostAddress + this.endpointUrl + "/details/byUser/" + orderCode);
  }

  // TODO delete unnecessary/duplicated endpoints
  buy(order: Order): Observable<Order> {
    return this._http.post<Order>(this.hostAddress + this.endpointUrl + "/buy/", order);
  }


}
