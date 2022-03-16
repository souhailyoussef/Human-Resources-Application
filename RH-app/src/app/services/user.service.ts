import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { baseUrl, environment } from 'src/environments/environment';
import { User } from '../user';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }
  //TODO : make a function to get the current username or pass it from login component to navbar!
  
  public getUserDetails(username : string) : Observable<User> {
    return this.http.get<User>(`${baseUrl}/api/users/user/${username}`);
    
  }
}
