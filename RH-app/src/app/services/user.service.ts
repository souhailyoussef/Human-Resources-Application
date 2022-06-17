import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import { baseUrl, environment } from 'src/environments/environment';
import { User } from '../user';
import { map } from 'rxjs/operators';
import * as internal from 'stream';
import { LoginuserService } from './loginuser.service';


export interface Repartition {
  female : number;
  male : number;
  total : number;
}


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient,private loginUserService : LoginuserService) { }
  //TODO : make a function to get the current username or pass it from login component to navbar!
  
  public getUserDetails(username : string) : Observable<User> {
    return this.http.get<User>(`${baseUrl}/api/users/user/${username}`);
    
  }

  public getCurrentUserDetails() : Observable<User> {
    return this.http.get<User>(`${baseUrl}/api/users/user/${this.loginUserService.currentUserValue.username}`);

  }

  public getGenderRepartition() : Observable<Repartition> {
      return this.http.get<Repartition>(`${baseUrl}/api/users/gender`);
  }
  public getBirthdays() : Observable<Array<string>> {
    return this.http.get<Array<string>>(`${baseUrl}/api/users/birthdays`)
  }

  public getCurrentTasksAndProjects(id : number, deadline:string) : Observable<any> {
    let params = new HttpParams().set("id", id)
                                  .set("deadline",deadline)
    return this.http.get<any>(`${baseUrl}/api/user/projects`,{params})
  }

  public getCurrentUserImputations(start_date:string, end_date:string) : Observable<any> {
    let params = new HttpParams().set("username",this.loginUserService.currentUserValue.username)
    .set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<any>(`${baseUrl}/api/imputations`,{params})
  }
}

