import {
  HttpInterceptor, HttpRequest, HttpHandler,
  HttpEvent, HttpErrorResponse
} from '@angular/common/http';
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

  isBlobError(err: any) {
    return err instanceof HttpErrorResponse && err.error instanceof Blob && err.error.type === 'application/json';
  }

  parseErrorBlob(err: HttpErrorResponse): Observable<any> {
    const reader: FileReader = new FileReader();
    const obs = new Observable((observer: any) => {
      reader.onloadend = (e) => {
        const newError = new HttpErrorResponse({
          ...err,
          error: JSON.parse(reader.result as string),
        });
        this.handleError(newError);
        observer.error(newError);
        observer.complete();
      };
    });
    reader.readAsText(err.error);
    return obs;
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<any> {
    return next.handle(req).pipe(catchError(err => {
      if (!this.isBlobError(err)) {
        return this.handleError(err);
      } else {
        return this.parseErrorBlob(err);
      }
    }));
  }

  handleError(error: HttpErrorResponse) {
    switch (error.status) {
      case 0: {
        //only one modal even if more than 1 error to prevent modal spam
        const modalRef = this.modalService.open(ErrorModalComponent);
        modalRef.componentInstance.title = "Error occured!";
        modalRef.componentInstance.message = "No connection to backend.";
        break;
      }
      case 401: {
        //only one modal even if more than 1 error to prevent modal spam
        const modalRef = this.modalService.open(ErrorModalComponent);
        modalRef.componentInstance.title = "Error occured!";
        modalRef.componentInstance.message = error.error.error;
        break;
      }
      case 403: {
        //only one modal even if more than 1 error to prevent modal spam
        const modalRef = this.modalService.open(ErrorModalComponent);
        modalRef.componentInstance.title = "Error occured!";
        modalRef.componentInstance.message = "Access denied.";
        break;
      }
      case 422: case 400: {
        if (Array.isArray(error.error)) {
          const errorArray = error.error as Array<ErrorModel>;
          errorArray.forEach(el => {
            const modalRef = this.modalService.open(ErrorModalComponent);
            modalRef.componentInstance.title = "Error occured!";
            modalRef.componentInstance.message = el.message;
          });
        } else {
          const err = error.error as ErrorModel;
          const modalRef = this.modalService.open(ErrorModalComponent);
          modalRef.componentInstance.title = "Error occured!";
          modalRef.componentInstance.message = err.message;
        }
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
