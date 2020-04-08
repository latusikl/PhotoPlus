import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { ErrorModel } from '../models/error/errormodel';

export abstract class AbstractService<T> {

  constructor(protected _http: HttpClient, protected endpointUrl: string) { }

  public getAll(): Observable<T[]>  {
    return this._http.get<T[]>(this.endpointUrl + "/all")
      .pipe(retry(2), catchError(this.handleError));
  }

  public getSingle(code: number): Observable<T>  {
    return this._http.get<T>(this.endpointUrl + "/" + code)
      .pipe(retry(2), catchError(this.handleError));

  }

  public post(item: T): Observable<T[]> {
    return this._http.post<T[]>(this.endpointUrl, [item])
      .pipe(retry(2), catchError(this.handleError));
  }

  public patch(code: number, item: T): Observable<T> {
    return this._http.patch<T>(this.endpointUrl + "/" + code, item)
      .pipe(retry(2), catchError(this.handleError));
  }

  public delete(code: number): Observable<T> {
    return this._http.delete<T>(this.endpointUrl + "/" + code)
      .pipe(retry(2), catchError(this.handleError));
  }

  private handleError(errorResponse: HttpErrorResponse) {

    const errorArray = errorResponse.error as Array<ErrorModel>;
    console.error(`Http code: ${errorResponse.status}.`);
    errorArray.forEach(el => {
      console.error(
        `Error: ${el.error}\n`
      + `Object: ${el.object}\n`
      + `Message: ${el.message}\n`);
    });

    return throwError("An error occured.");
  };

}
