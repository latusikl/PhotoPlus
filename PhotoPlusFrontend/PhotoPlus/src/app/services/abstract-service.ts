import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { retry } from 'rxjs/operators';

export abstract class AbstractService<T> {

  constructor(protected _http: HttpClient, protected endpointUrl: string) { }

  public getAll(): Observable<T[]>  {
    return this._http.get<T[]>(this.endpointUrl + "/all").pipe(retry(2));
  }

  public getSingle(code: number): Observable<T>  {
    return this._http.get<T>(this.endpointUrl + "/" + code).pipe(retry(2));
  }

  public post(item: T): Observable<T[]> {
    return this._http.post<T[]>(this.endpointUrl, [item]).pipe(retry(2));
  }

  public patch(code: number, item: T): Observable<T> {
    return this._http.patch<T>(this.endpointUrl + "/" + code, item).pipe(retry(2));
  }

  public delete(code: number): Observable<T> {
    return this._http.delete<T>(this.endpointUrl + "/" + code).pipe(retry(2));
  }

}
