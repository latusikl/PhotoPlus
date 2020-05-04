import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  protected hostAddress = environment.hostAddress;

  constructor(private http:HttpClient) { }

  
}
