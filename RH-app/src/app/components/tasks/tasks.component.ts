import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { UserService } from 'src/app/services/user.service';
import { Imputation } from 'src/app/imputation';
import { TaskModalComponent } from '../task-modal/task-modal.component';

const CURRENT_DATE= new Date()

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
/*
  weeklyColumns : string[] = ['task','sunday','monday','tuesday','wednesday','thursday','friday','saturday']
  current_date : Date = new Date()
  CURRENT_DATE = CURRENT_DATE
  week: Date[] = [];
  imputations : Imputation[] = []
  tasknames : any

  constructor(private userService : UserService,private matDialog : MatDialog) { }

  ngOnInit(): void {

    this.week = this.getWeek(this.current_date);
    this.getWeeklyImputations()

  }

  ngOnChanges() {

  }
  getWeek(fromDate : Date){
    var sunday = new Date(fromDate.setDate(fromDate.getDate()-fromDate.getDay()))
    sunday.setHours(0,0,0,0)
    var result = [new Date(sunday)];
    while (sunday.setDate(sunday.getDate()+1) && sunday.getDay()!==0) {
     result.push(new Date(sunday));
    }

    return result;
   }
   nextWeek() : Date {

     this.current_date= new Date(this.current_date.getFullYear(), this.current_date.getMonth(), this.current_date.getDate()+7);
     this.week = this.getWeek(this.current_date);
     this.getWeeklyImputations()

     return this.current_date
   }
   previousWeek() : Date {

    this.current_date= new Date(this.current_date.getFullYear(), this.current_date.getMonth(), this.current_date.getDate()-7);
    this.week = this.getWeek(this.current_date);
    this.getWeeklyImputations()

    return this.current_date
  }
  currentWeek() : Date {
    this.current_date=CURRENT_DATE
    this.week=this.getWeek(CURRENT_DATE)
    this.getWeeklyImputations()
    return CURRENT_DATE;
  } 
  convertDateToText(date : Date) : string {

      var text  : string ;
      if (date.getDate()<10 && date.getMonth()+1 < 10) {
        text=date.getFullYear()+'-'+'0'+(date.getMonth()+1)+'-'+'0'+date.getDate();
      }
      else if (date.getDate()<10 && date.getMonth()+1 >=10)
      {
        text=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+'0'+date.getDate();
      }
      else if (date.getDate()>=10 && date.getMonth()+1 <10){  
        text=date.getFullYear()+'-'+'0'+(date.getMonth()+1)+'-'+date.getDate();
      }
      else {
        text=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
      }
      return text
      
  }
  getWeeklyImputations() {
    this.userService.getCurrentUserImputations(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe((data : any) => {
    this.imputations=data
    var set : Array<any> = new Array()
    //step 1 : fetch all tasks in an array
    for (var i=0;i<this.imputations.length;i++) {
      set.push(this.imputations[i].task)
    }
    //transform the array of tasks into array of unique tasks
    set = [...new Map(set.map((item : any)=>
      [item['name'], item])).values()];
    this.tasknames=  Array.from(set);
   
      })
    }

    getImputation(taskname: string, day: string) : Imputation |undefined{
      for (var i=0;i<this.imputations.length;i++) {
        if(this.imputations[i].task['name']==taskname && this.imputations[i].day==day) {
          return this.imputations[i];
        }
        
      }
          
      return undefined      
     
    }
    getTotal(day : string) : number{
      let sum=0;
      for (var i=0;i<this.imputations.length;i++) {
        if(this.imputations[i].day==day) {
          sum+= this.imputations[i].workload;
        }
        
      }
        return sum
    }
    
    openDialog(imputation ?: Imputation){
      let  dialogRef 
      if (typeof imputation !== 'undefined') {
          dialogRef =  this.matDialog.open(TaskModalComponent,{
          data : {
            event :'edit',
            imputation:imputation
          },
          disableClose:true,
          width:'500px',
          height:'500px'
         
        });
       
      }
      else {
          dialogRef =  this.matDialog.open(TaskModalComponent,{
          data : {
            event :'add',
          },
          disableClose:true,
          width:'500px',
          height:'500px'
         
        });
       
      }
    
      dialogRef.afterClosed().subscribe((result : any) => {
        console.log('The dialog was closed');
      });
   
      }
   
   
*/
}
