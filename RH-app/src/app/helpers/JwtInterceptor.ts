import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginuserService } from '../services/loginuser.service';
import { baseUrl } from 'src/environments/environment';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private loginUserService: LoginuserService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add auth header with jwt if user is logged in and request is to the api url
        const currentUser = this.loginUserService.currentUserValue;
        const isLoggedIn = currentUser && currentUser.access_token;
        const isApiUrl = request.url.startsWith(baseUrl);
       
        if (isLoggedIn && isApiUrl) {
            
                const cloned = request.clone({
                    headers: request.headers.set("Authorization",
                        "Bearer " + currentUser.access_token)
                });
    
                return next.handle(cloned);
            
        }

        return next.handle(request);
    }
}