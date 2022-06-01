import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { LoginuserService } from '../services/loginuser.service';
import { baseUrl } from 'src/environments/environment';


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private loginUserService: LoginuserService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (request.url.startsWith(`${baseUrl}/api/nodes`)) {
                //console.log("intercepting nodes requests!!!!")
            }

            if (err.status === 401) {
                // auto logout if 401 response returned from api
                this.loginUserService.logout();
                location.reload();
            }

            const error = err.error.message || err.statusText;
            return throwError(error);
        }))
    }
}