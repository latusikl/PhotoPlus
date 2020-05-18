import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { HttpClient } from '@angular/common/http';
import { Address } from 'src/app/models/address/address';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class AddressService extends AbstractService<Address> {

  constructor(http: HttpClient) {
    super(http, "address");
  }
  byUser(code: string): Observable<Address[]> {
    return this._http.get<Address[]>(this.hostAddress + this.endpointUrl + "/byUser/" + code);
  }

}
