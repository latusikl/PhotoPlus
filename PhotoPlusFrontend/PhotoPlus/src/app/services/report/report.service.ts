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
      responseType: 'blob' as "json"
    });
  }
  getProductReport(productCode: string, fromDate: Date, toDate: Date): Observable<Blob>{
    return this.http.get<Blob>(this.hostAddress + "report" + "/product" + `?code=${productCode}&beginDate=${fromDate}&endDate=${toDate}`,  {
      responseType: 'blob' as "json"
    });
  }
}
