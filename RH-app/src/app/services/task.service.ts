import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { numberFormat } from 'highcharts';
import { Observable } from 'rxjs';
import { baseUrl } from 'src/environments/environment';
import { LoginuserService } from './loginuser.service';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient, private loginUserService : LoginuserService) { }

  public getTask(id : number) : Observable<Task> {
    return this.http.get<Task>(`${baseUrl}/api/task/${id}`);
    
  }
  public postImputation(form : FormGroup) : Observable<any> {
    const body = { task_id : form.controls['name'].value.id,
      day : form.controls['day'].value,
      workload : form.controls['workload'].value,
      comment : form.controls['comment'].value,
      username : this.loginUserService.currentUserValue.username};    
    return this.http.post<any>(`${baseUrl}/api/imputation/save`,body );
  }

}
