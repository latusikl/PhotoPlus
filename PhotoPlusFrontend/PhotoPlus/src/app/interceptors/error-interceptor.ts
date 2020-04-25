import { HttpInterceptor, HttpRequest, HttpHandler,
  HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorModalComponent } from '../components/error-modal/error-modal.component';
import { Injectable } from '@angular/core';
import { ErrorModel } from '../models/error/errormodel';

@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private modalService: NgbModal) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    return next.handle(req).pipe(catchError(err => this.handleError(err)))
  };

  handleError(error: HttpErrorResponse) {
    const modalRef = this.modalService.open(ErrorModalComponent);
    modalRef.componentInstance.title = "Error occured!";
    switch(error.status) {
      case 0: {
        modalRef.componentInstance.message = "No connection to backend.";
        break;
      }
      case 401: {
        modalRef.componentInstance.message = "Bad login or password. Please try again.";
        break;
      }
      case 403: {
        modalRef.componentInstance.message = "Access denied.";
        break;
      }
      case 422: {
        const errorArray = error.error as Array<ErrorModel>;
        errorArray.forEach(el => {
          modalRef.componentInstance.message = el.message;
        });
        break;
      }
      default: {
         //statements;
         break;
      }
    }
    return throwError(error);
  }
}
