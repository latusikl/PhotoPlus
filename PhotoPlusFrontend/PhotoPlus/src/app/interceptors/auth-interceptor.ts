import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

export class AuthInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>,
              next: HttpHandler): Observable<HttpEvent<any>> {

        const token = sessionStorage.getItem("token");
        /* for authorization in microsoft edge
        req.headers.set('Access-Control-Allow-Origin', '*');
        req.headers.set('Access-Control-Allow-Headers', 'Origin, Content-Type, X-Auth-Token')
        req.headers.set('Access-Control-Allow-Methods', 'GET, POST, PATCH, PUT, DELETE, OPTIONS');
        req.headers.set('Access-Control-Expose-Headers', '*');
        */
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


