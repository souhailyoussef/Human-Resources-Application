import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { tomorrow } from '@igniteui/material-icons-extended';
import { getDay } from 'date-fns';
import { truncate } from 'lodash';
import { start } from 'repl';
import { BehaviorSubject, catchError, filter, map, Observable, of, Subject, tap } from 'rxjs';
import { baseUrl } from 'src/environments/environment';
import { EventEmitter } from 'stream';
import { Client } from '../client';
import { Imputation } from '../imputation';
import { Leave } from '../leave';
import { Project } from '../Project';
import { LoginuserService } from './loginuser.service';


export class Cell {
  id : string;
  selected : boolean
  value : Imputation | undefined;
  constructor(id: string, value : Imputation |any) {
    this.id=id;
    this.selected=false;
    this.value=value;
  }
}

export interface TableElement {
  task : Task;
  monday : Cell ;
  tuesday : Cell ;
  wednesday : Cell;
  thursday : Cell;
  friday : Cell;
  saturday : Cell;
  Sunday : Cell;
  total : number
}
export interface Break {
  total : number;
  monday : number;
  tuesday : number;
  wednesday : number;
  thursday : number;
  friday : number;
  saturday : 0;
  sunday : 0;
  isLeave : true;
}
export interface Group {
  project: string;
  isGroup: boolean;
}

export class ClientData {
  client : Client;
  project : Project;
}

@Injectable({
  providedIn: 'root'
})


export class timesheetService {




  filters$: Observable<any>;
  private filtersSubject = new Subject<any>();

  private currentFilterSubject: BehaviorSubject<any>;
  public currentFilter: Observable<any>; 


  private _refreshNeeded$ = new Subject<void>();



  get refreshNeeded$() {
    return this._refreshNeeded$;
  }


  constructor(private http: HttpClient, private loginUserService : LoginuserService) { 
    this.filters$ = this.filtersSubject.asObservable();

  }

  
  updateFilter(data : FormGroup) {

    this.filtersSubject.next(data);
    this.currentFilterSubject = new BehaviorSubject<any>(data);
    this.currentFilter = this.currentFilterSubject.asObservable()


}

  loadData(start_date:string, end_date:string) : Observable<any>  {
    let params = new HttpParams().set("username",this.loginUserService.currentUserValue.username)
    .set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<any>(`${baseUrl}/api/imputations`,{params})
    
   
  }

  public getWeeklyImputations(start_date:string, end_date:string) : Observable<any> {
    let params = new HttpParams().set("username",this.loginUserService.currentUserValue.username)
    .set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<any>(`${baseUrl}/api/weekly/imputations`,{params})
  }

  public getMonthlyImputations(start_date:string, end_date:string, project_id:number) : Observable<any> {
    let params = new HttpParams().set("project_id",project_id)
    .set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<any>(`${baseUrl}/api/monthly/imputations`,{params})
  }

  public getHolidays(start_date:string, end_date:string) : Observable<string[]> {
    let params = new HttpParams().set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<any>(`${baseUrl}/api/holidays`,{params}).pipe(map(days =>  days?.response?.toLowerCase().split(',')))
  }

  public getMonthlyNonBusinessDays(start_date:string, end_date:string, employee_id : number ) : Observable<number[]> {
      let params = new HttpParams().set("employee_id",employee_id)
      .set("start_date",start_date)
      .set("end_date",end_date)
      return this.http.get<number[]>(`${baseUrl}/api/monthly/nonBusinessDays`,{params})

  }
  

  
 

  public postImputation(form : FormGroup) : Observable<any> {
    const body = { task_id : form.controls['name'].value.task_id || form.controls['name'].value.id,
      day : form.controls['day'].value,
      workload : form.controls['workload'].value,
      comment : form.controls['comment'].value,
      username : this.loginUserService.currentUserValue.username};    
    return this.http.post<any>(`${baseUrl}/api/imputation/save`,body )
    .pipe(
      tap(() => {
        this._refreshNeeded$.next();
      }) 
    );
  }

  public getLeaves(start_date:string, end_date:string)  : Observable<Break>{
    let monday = new Date(start_date);
    let sunday = new Date(end_date);
    let username = this.loginUserService.currentUserValue.username
    let params = new HttpParams().set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<Leave[]>(`${baseUrl}/api/leaves/approved/${username}`,{params}).pipe(map((data)=>
     {return this.transformData(data)}))
    }

    public getUserLeaves(start_date:string, end_date:string, employee_id : number)  : Observable<Break>{
      let monday = new Date(start_date);
      let sunday = new Date(end_date);
      let params = new HttpParams().set("start_date",start_date)
      .set("end_date",end_date)
      return this.http.get<Leave[]>(`${baseUrl}/api/leaves/approved/id/${employee_id}`,{params}).pipe(map((data)=>
       {return this.transformData(data)}))
      }

  transformData(leaves : any[]) : Break {
    var array =[0,0,0,0,0]
  
    let monday = leaves.find(elt => elt.day.trim()=='monday')
    let tuesday = leaves.find(elt => elt.day.trim()=='tuesday')
    let wednesday = leaves.find(elt => elt.day.trim()=='wednesday')
    let thursday = leaves.find(elt => elt.day.trim()=='thursday')
    let friday = leaves.find(elt => elt.day.trim()=='friday')

    array[0]=monday?.leave_duration || 0
    array[1]=tuesday?.leave_duration || 0
    array[2]=wednesday?.leave_duration || 0
    array[3]=thursday?.leave_duration || 0
    array[4]=friday?.leave_duration || 0

    var result : Break = {monday:array[0],tuesday:array[1],wednesday:array[2],thursday:array[3],friday:array[4], total: array.reduce((a,b)=>a+b),saturday:0 , sunday:0 , isLeave:true}
    return result

  }

  public getAllClientsAndProjects() : Observable<ClientData[]> {
    return this.http.get<ClientData[]>(`${baseUrl}/api/clients`)
  }
}


