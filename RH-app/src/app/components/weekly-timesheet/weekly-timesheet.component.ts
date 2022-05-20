import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { UserService } from 'src/app/services/user.service';
import { Imputation } from 'src/app/imputation';
import { TaskModalComponent } from '../task-modal/task-modal.component';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Task } from 'src/app/task';
import { TaskService } from 'src/app/services/task.service';


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
export interface GroupBy {
  initial: string;
  isGroupBy: boolean;
  project_id ?: number;
}
export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: (PeriodicElement | GroupBy)[] = [
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {initial: 'C', isGroupBy: true},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {initial: 'F', isGroupBy: true},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {initial: 'H', isGroupBy: true},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {initial: 'L', isGroupBy: true},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {initial: 'N', isGroupBy: true},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {initial: 'O', isGroupBy: true},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'}]




@Component({
  selector: 'app-weekly-timesheet',
  templateUrl: './weekly-timesheet.component.html',
  styleUrls: ['./weekly-timesheet.component.css']
})
export class WeeklyTimesheetComponent implements OnInit {
  @Input('filters') filters : FormGroup;
  
  weeklyColumns : string[] = ['task','monday','tuesday','wednesday','thursday','friday','saturday','sunday']
  current_date : Date = new Date()
  CURRENT_DATE= new Date()
   week: Date[] 
   imputations : Imputation[] = []
   tasknames : any;
  dataSource : any[] = [] // datasource format : [[task,[imputations...]],[task2,[imputations...]]]]
                               
  toggleButton : boolean;
  editButtonValue : string = "edit"
  previousSelected: Cell[] = [];



  //////
     
  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  dataSource2 = ELEMENT_DATA;


  constructor(private userService : UserService,private matDialog : MatDialog, private formBuilder : FormBuilder,
     private taskService : TaskService) { 

  }

  isGroup(index : number, item : any): boolean{
    return item.isGroupBy  
  }

  ngOnInit(): void {
    this.week=this.getWeek(this.current_date);

    this.getWeeklyImputations()

    this.toggleButton=false;
  
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
    this.current_date=this.CURRENT_DATE
    this.week=this.getWeek(this.CURRENT_DATE)
    this.getWeeklyImputations()
    return this.CURRENT_DATE;
  } 
  public convertDateToText(date : Date) : string {

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
    // TODO : create ADD TASK button so we can have imputations for all tasks
    this.userService.getCurrentUserImputations(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe((data : any) => {
    //filtering is done at this level
    if (this.filters.controls['tasks'].value!=null) {
      var tasks_id = this.filters.controls['tasks'].value.map((task : any) => task.task_id)
      this.imputations=data.filter((imputation : Imputation) => tasks_id.indexOf(imputation.task.id)!=-1)
      
    }
    else {

      this.imputations=data
    }
   
    this.dataSource=[]

    var set : Array<any> = new Array()
    //step 1 : fetch all tasks in an array
    for (var i=0;i<this.imputations.length;i++) {
      set.push(this.imputations[i].task)
    }
    //transform the array of tasks into array of unique tasks
    set = [...new Map(set.map((item : any)=>
      [item['name'], item])).values()];
    this.tasknames=  Array.from(set);
 
    for(let project of this.filters.controls['projects'].value ||[] )
    {
    
    for (var i=0;i<this.tasknames.length;i++) {

      var current_task = this.tasknames[i]
      
      for(let task of this.filters.controls['tasks'].value) {
        if (task.task_id==current_task.id && task.project_id==project.project_id) {
          this.dataSource.push({initial: project.project_name, isGroupBy: true , project_id : project.project_id})

          let correspending_imputations=this.imputations.filter(function(value : Imputation) {
            return value.task.name == current_task.name});
    
          var weekly_imputations = this.createWeeklyArray(current_task,correspending_imputations)
          var list = [current_task,weekly_imputations]
       
          this.dataSource.push(list)
          
        }
      }
    
    }
    }
   

      })
    
      
    }

  createWeeklyArray(task : Task,imputations : Imputation[]) : Cell[]  {
    var result : Cell[]  = []
        for (let i=0;i<this.week.length;i++) {
          let day =this.convertDateToText(this.week[i])
          result[i]=new Cell(task.name+'-'+i,undefined)
          for (let j=0;j<imputations.length;j++)
          {
            if (imputations[j].day==day) {
              result[i]= new Cell(task.name+'-'+i,imputations[j])
            }
          }
      
    }

    return result

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
      for (var i=0;i<this.dataSource.length;i++) {
          for(var j=0;j<this.dataSource[i][1]?.length;j++) {

            if(this.dataSource[i][1][j].value?.day==day) {
              sum+= this.dataSource[i][1][j]?.value.workload | 0;
  
            
          }
        
        }
          
        
       
        
      }
        return sum
    }
    



    openDialog(imputation ?: any){
      let  dialogRef 
      

      if (typeof imputation == 'object') {

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
            task : imputation
          },
          disableClose:true,
          width:'500px',
          height:'500px'
         
        });
       
      }
    
      dialogRef.afterClosed().subscribe((result : any) => {
        this.getWeeklyImputations()
      });
   
      }
    
      
      
    //** THE MULTI SELECT CODE HERE **


      toggleSelected(obj :Cell , event : any) {
          //TODO : add row selection or drag and ctrl event
          if (event.shiftKey) {
          obj.selected = !obj.selected;
          if(obj.selected && !this.previousSelected.includes(obj)) { this.previousSelected.push(obj)}
    

    
        } else if (event.ctrlKey) {
          obj.selected = !obj.selected;
          if(obj.selected && !this.previousSelected.includes(obj)) { this.previousSelected.push(obj)}

        } else {
            //set all previous cells to selected = false;
            this.dataSource.forEach(row => {
              if (!row?.isGroupBy) {
                row[1].forEach((cell : Cell) => {
                  cell.selected=false
  
                })

              }
              
            })
            obj.selected = !obj.selected;
            this.previousSelected=[]
            this.previousSelected.push(obj);
        }

      }

      fillCells(obj : any, date : Date , event : any) {
   
        this.previousSelected.forEach(elt => {
          if (elt.value!=undefined) {
            elt.value.workload=event.target.value
          }
          else if(elt.value===undefined || elt.value=="") {
            
           elt.value= new Imputation(obj,this.convertDateToText(date),event.target.value)
          }
          })
        }

        enableEdit() {
          this.toggleButton=!this.toggleButton;
          if (this.toggleButton) this.editButtonValue="cancel";
          else this.editButtonValue="edit"
          
        }

        saveChanges() {
          for (var i=0;i<this.dataSource.length;i++) {

              for(var j=0;j<this.dataSource[i][1]?.length;j++) {
                var cell : Cell = this.dataSource[i][1][j];
                if (cell.value !==undefined)
                {
                  let imputationForm= this.formBuilder.group({
                    'name': [cell.value.task, [Validators.required]],
                    'day': [cell.value.day,[Validators.required]],
                    'workload': [cell.value.workload,[Validators.required,Validators.max(60*8),Validators.min(30)]],
                    'comment': ['']
                  });
                  this.taskService.postImputation(imputationForm).subscribe() //TODO : add popup modal for success and error
                }
              }
            

           
        }
        this.toggleButton=false;
           this.editButtonValue="edit"
      }

      addTask() {
        this.openDialog(undefined)
      }
      printPreview() {}


  
   
     

      



    }
