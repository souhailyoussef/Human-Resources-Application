import { HttpClient, HttpClientModule, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { User } from '../user';
import { baseUrl } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoginuserService {
  private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>; 

    constructor(private http: HttpClient) {

        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser') || '{}'));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    login(username: string, password: string) {

        const body = new HttpParams()
         .set('username', username)
        .set('password', password)

        return this.http.post<any> (`${baseUrl}/api/login`, body, {
        headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
          })
          .pipe(map((user: User) => {
            // store user details and jwt token in local storage to keep user logged in between page refreshes
            localStorage.setItem('currentUser', JSON.stringify(user));
            this.currentUserSubject.next(user);
            return user;


     
    }));
}
    
    public getUsers() : Observable<any> {
        return this.http.get(`${baseUrl}/api/users`);

    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null as any);
    }
  
}
