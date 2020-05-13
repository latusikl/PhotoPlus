import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { HttpClient } from '@angular/common/http';
import { Address } from 'src/app/models/address/address';

@Injectable({
  providedIn: 'root'
})
export class AddressService extends AbstractService<Address> {

  constructor(http: HttpClient) {
    super(http, "address");
  }

}
