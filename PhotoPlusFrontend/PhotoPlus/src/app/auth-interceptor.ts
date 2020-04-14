import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

export class AuthInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>,
              next: HttpHandler): Observable<HttpEvent<any>> {

        const token = localStorage.getItem("token");

        if (token != null) {
            console.log("Interceptor called.");
            const cloned = req.clone({
                headers: req.headers.set("Authorization",
                    token)
            });

            return next.handle(cloned);
        } else {
            return next.handle(req);
        }
    }
}


