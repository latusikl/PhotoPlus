import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { HttpClient } from '@angular/common/http';
import { Order } from 'src/app/models/order/order';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class OrderService extends AbstractService<Order> {

  constructor(http: HttpClient) {
    super(http, "order");
  }

  buy(order: Order): Observable<Order> {
    return this._http.post<Order>(this.hostAddress + this.endpointUrl + "/buy/", order);
  }
}
