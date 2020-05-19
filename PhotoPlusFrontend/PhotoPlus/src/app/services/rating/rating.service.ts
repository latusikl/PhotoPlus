
import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Rating } from 'src/app/models/rating/rating';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PageInfo } from 'src/app/models/page-info/page-info';
import { Observable } from 'rxjs/internal/Observable';
@Injectable({
  providedIn: 'root'
})
export class RatingService extends AbstractService<Rating>{

  constructor(http: HttpClient) {
    super(http, "rating");
  }

  getRatingsPage(page: number, sortedBy: string, productCode: string) {
    let params = new HttpParams().set("sortedBy", sortedBy).set("productCode", productCode)
    return this._http.get<Rating[]>(this.hostAddress + this.endpointUrl + "/all/" + page, { params: params });
  }

  public getPageCountRating(productCode: string): Observable<PageInfo> {
    let params = new HttpParams().set("productCode", productCode)
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + '/page/count', { params: params });
  }

}
