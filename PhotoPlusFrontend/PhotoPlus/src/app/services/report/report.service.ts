import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  protected hostAddress = environment.hostAddress;

  constructor(private http:HttpClient) { }

  getProfitReport(fromDate: Date, toDate: Date): Observable<Blob>{
    return this.http.get<Blob>(this.hostAddress + "report" + "/profit" + `?beginDate=${fromDate}&endDate=${toDate}`, {
      headers: new HttpHeaders().set('Accept', 'application/pdf'),
      responseType: 'blob' as "json"
    });
  }
  getProductReport(productCode: string): Observable<Blob>{
    return this.http.get<Blob>(this.hostAddress + "report" + "/product" + `?code=${productCode}`,  {
      headers: new HttpHeaders().set('Accept', 'application/pdf'),
      responseType: 'blob' as "json"
    });
  }
}
