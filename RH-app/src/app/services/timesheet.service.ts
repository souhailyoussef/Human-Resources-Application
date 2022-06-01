import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { tomorrow } from '@igniteui/material-icons-extended';
import { truncate } from 'lodash';
import { BehaviorSubject, catchError, filter, map, Observable, of, Subject, tap } from 'rxjs';
import { baseUrl } from 'src/environments/environment';
import { EventEmitter } from 'stream';
import { Imputation } from '../imputation';
import { Leave } from '../leave';
import { LoginuserService } from './loginuser.service';

const CHECK_IN_TIME = 8*60*60*1000; // 8 am : in milliseconds

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

@Injectable({
  providedIn: 'root'
})
export class timesheetService {

  filters$: Observable<any>;
  private filtersSubject = new Subject<any>();

  private _refreshNeeded$ = new Subject<void>();

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }


  constructor(private http: HttpClient, private loginUserService : LoginuserService) { 
    this.filters$ = this.filtersSubject.asObservable();
  }

  updateFilter(data : FormGroup) {

    this.filtersSubject.next(data);
}

  loadData(start_date:string, end_date:string) : Observable<(TableElement | Group |Break)[]>  {
    let params = new HttpParams().set("username",this.loginUserService.currentUserValue.username)
    .set("start_date",start_date)
    .set("end_date",end_date)
    return this.http.get<(TableElement | Group |Break)[]>(`${baseUrl}/api/imputations`,{params})
   
  }

  public postImputation(form : FormGroup) : Observable<any> {
    const body = { task_id : form.controls['name'].value.id,
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
     {return this.transformData(data,monday,sunday)}))
    }

  transformData(leaves : Leave[], monday:Date, sunday:Date) : any {
    var array =[0,0,0,0,0]
  
    leaves.forEach(leave => {
      //THIS WORKS!!!!
      let leave_array = [0,0,0,0,0]
      let leave_start_date = (new Date(leave.start_date))
      let leave_end_date = (new Date(leave.end_date))

      const MONDAY = new Date(monday.getTime()) //create constant copy of current_week monday
      let check = this.isFirstOrLastDay(MONDAY,leave)
      var next_day = MONDAY; 
      next_day.setDate(next_day.getDate() + 1) 
   
      for(let i=0;i<5;i++) {
        if(check=='first') {leave_array[i]=this.extractHours(leave_start_date,false); }
        else if (check=='last') {leave_array[i]=this.extractHours(leave_end_date,true) ; }
        else if(check=='between') {leave_array[i]=8;  }
        check = this.isFirstOrLastDay(next_day,leave);  next_day.setDate(next_day.getDate() + 1)
      }
      array = array.map((element,index) => {
        return element+leave_array[index]})

    } )

    let result : Break = {
      monday: array[0], tuesday: array[1], wednesday: array[2], thursday: array[3], friday: array[4], saturday: 0, sunday: 0, total: array.reduce((a, b) => a + b, 0), isLeave: true}
    return result

    
  }



  isFirstOrLastDay(date : Date,leave : Leave) : string{
          //check if date provided is the first or last day of the Leave
          //this works
      let leave_start_date= new Date(leave.start_date)
      leave_start_date.setHours(0,0,0,0)
      let leave_end_date = new Date(leave.end_date)
      leave_end_date.setHours(0,0,0,0)
      date.setHours(0,0,0,0)
      let leave_start_date_string = new Date(leave.start_date).toDateString()
      let leave_end_date_string = new Date(leave.end_date).toDateString() 
      let result = (date.toDateString() == leave_start_date_string) ? 'first' :
      (date.toDateString() == leave_end_date_string) ? 'last' :
      (date < leave_start_date || date > leave_end_date) ? 'none' :
      'between';
      return result
  }

  
    extractHours(day : Date, param : boolean) : number {
        //calculates how much time did the employee skip in hours
        //TODO : round this up 
        let hours = day.getHours();

        if(param) { //date is end date
          let diff = (hours+(day.getMinutes()/60) - 8)
          if (hours>=14) return diff- 2 //substract 2 hours of lunch
          return diff

        }
        //date is start date
        let diff = 18-(hours+(day.getMinutes()/60) )
        if (hours<=12) return diff- 2 //substract 2 hours of lunch
        return diff
        
    }
   
  
}


