import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Order } from 'src/app/models/order/order';
import { HttpClient } from '@angular/common/http';
import { OrderStatus } from 'src/app/models/order-status/order-status';
import { PageInfo } from 'src/app/models/page-info/pageInfo';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService extends AbstractService<Order> {

  constructor(private http:HttpClient) { 
    super(http,"order");
  }

  getPageCountForOrdersOfStatus(status: OrderStatus): Observable<PageInfo>{
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + "/page/count?orderStatus=" + status);
  }

  getPageByOrderStatus(pageNumber: number, status: OrderStatus){
    return this._http.get(this.hostAddress + this.endpointUrl + "/" + pageNumber + "?orderStatus=" + status);
  }

}
