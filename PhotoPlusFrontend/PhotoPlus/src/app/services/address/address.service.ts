import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Address } from 'src/app/models/address/address';

@Injectable({
  providedIn: 'root'
})
export class AddressService extends AbstractService<Address> {

  constructor(http:HttpClient) {
    super(http,"address")
  }

  getAllByUser(userCode: string):Observable<Address[]> {
    return this._http.get<Address[]>(this.hostAddress + this.endpointUrl + '/byUser/' + userCode);
  }

  patchOwnAddress(addressCode: string, changedAddress:Address){
    return this._http.patch(this.hostAddress + this.endpointUrl + "/editAddress/" + addressCode, changedAddress);
  }
}
