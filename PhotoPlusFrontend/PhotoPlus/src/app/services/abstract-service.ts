import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export abstract class AbstractService<T> {

  protected hostAddress = environment.hostAddress;

  constructor(protected _http: HttpClient, protected endpointUrl: string) { }

  public getAll(): Observable<T[]>  {
    return this._http.get<T[]>(this.hostAddress + this.endpointUrl + "/all");
  }

  public getSingle(code: number): Observable<T>  {
    return this._http.get<T>(this.hostAddress + this.endpointUrl + "/" + code);
  }

  public post(item: T): Observable<T[]> {
    return this._http.post<T[]>(this.hostAddress + this.endpointUrl, [item]);
  }

  public patch(code: number, item: T): Observable<T> {
    return this._http.patch<T>(this.hostAddress + this.endpointUrl + "/" + code, item);
  }

  public delete(code: number): Observable<T> {
    return this._http.delete<T>(this.hostAddress + this.endpointUrl + "/" + code);
  }

}
