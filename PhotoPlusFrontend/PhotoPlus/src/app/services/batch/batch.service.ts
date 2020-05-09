import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { HttpClient } from '@angular/common/http';
import { Batch } from 'src/app/models/batch/batch';

@Injectable({
  providedIn: 'root'
})
export class BatchService extends AbstractService<Batch> {

  constructor(private httpClient: HttpClient) {
    super(httpClient, "batch");
   }
}
