import { environment } from '../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageInfo } from '../models/page-info/page-info';

export abstract class AbstractService<T> {

  protected hostAddress = environment.hostAddress;

  constructor(protected _http: HttpClient, protected endpointUrl: string) { }

  public getAll(): Observable<T[]>  {
    return this._http.get<T[]>(this.hostAddress + this.endpointUrl + '/all');
  }

  public getSingle(code: string): Observable<T>  {
    return this._http.get<T>(this.hostAddress + this.endpointUrl + '/' + code);
  }

  public getFromLink(link: string): Observable<T>  {
    return this._http.get<T>(link);
  }

  public getAllFromLink(link: string): Observable<T[]>  {
    return this._http.get<T[]>(link);
  }

  public post(item: T): Observable<HttpResponse<T>> {
    return this._http.post<T>(this.hostAddress + this.endpointUrl, item, { observe: 'response' });
  }

  public patch(code: string, item: T): Observable<T> {
    return this._http.patch<T>(this.hostAddress + this.endpointUrl + '/' + code, item);
  }

  public delete(code: string): Observable<T> {
    return this._http.delete<T>(this.hostAddress + this.endpointUrl + '/delete/' + code);
  }

  public getPageCount(): Observable<PageInfo> {
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + '/all/page/count');
  }

  public getPage(page: number): Observable<T[]> {
    return this._http.get<T[]>(this.hostAddress + this.endpointUrl + '/all/' + page);
  }
}
