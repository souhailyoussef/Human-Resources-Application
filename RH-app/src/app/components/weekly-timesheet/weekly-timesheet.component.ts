import { ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { UserService } from 'src/app/services/user.service';
import { Imputation } from 'src/app/imputation';
import { TaskModalComponent } from '../task-modal/task-modal.component';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Task } from 'src/app/task';
import { TaskService } from 'src/app/services/task.service';
import { Project } from '../timesheets/timesheets.component';
import { Break, timesheetService } from 'src/app/services/timesheet.service';
import { Leave } from 'src/app/leave';
import { filter, indexOf } from 'lodash';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { ViewChild } from '@angular/core';
import * as Excel from "exceljs";
import * as FileSaver from 'file-saver';






export class Cell {
  id : string;
  selected : boolean
  value : Imputation | undefined;
  constructor(id: string, value : Imputation |any) {
    this.id=id;
    this.selected=false;
    this.value=value;
    this.touched=false;
  }
  touched: boolean
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

export interface Group {
  project: string;
  isGroup: boolean;
}



const DATA : (TableElement | Group )[] = []

@Component({
  selector: 'app-weekly-timesheet',
  templateUrl: './weekly-timesheet.component.html',
  styleUrls: ['./weekly-timesheet.component.css'],

  
  
})
export class WeeklyTimesheetComponent implements OnInit {
  @Input("current_date") current_date : Date;
  @Input("taskGroups") taskGroups : any[];
  @Input("allProjects") allProjects : Project[]
  @ViewChild('pdfTable') pdfTable: ElementRef;



  filters : FormGroup;
  
  weeklyColumns2 : string[] = ['task','monday','tuesday','wednesday','thursday','friday','saturday','sunday','total']

  CURRENT_DATE= new Date()
  week: Date[] 
  imputations : Imputation[] = []
  tasknames : any =[];
  dataSource2 : any = []
    //{project : "project 1", isGroupBy: true},{project : "project 2",isGroupBy: true}               
  toggleButton : boolean;
  editButtonValue : string = "edit"
  previousSelected: Cell[] = [];
  break : Break = {monday:0,tuesday:0,wednesday:0,thursday:0,friday:0,saturday:0,sunday:0, total:0,isLeave:true};
  disabled=false
  totals = [0,0,0,0,0,0,0]
  touchedCells : Cell[] = []
  holidays : string[] = []
  tasks : Task[] = []
  weekly : any[] = []
  employee_full_name : string 
  manager_info : any
  client_info : any

  constructor(private matDialog : MatDialog, private formBuilder : FormBuilder,private alertService: AlertService,
     private timesheetService : timesheetService,private userService : UserService) { 

  }

  isGroup(index : number, item : any): boolean{
    return item.isGroup 
  }
  isLeave(index : number , item : any) : boolean {
    return item.isLeave
  }

  ngOnInit(): void {

    this.week=this.getWeek(this.current_date);
    this.timesheetService.filters$.subscribe((data) => {
      this.filters = data; 

      this.tasks = data.controls['tasks'].value
      this.loadData()
      this.getWeekly()
     this.timesheetService.refreshNeeded$.subscribe(() =>  {this.loadData()
    this.getWeekly()})

  })
  this.toggleButton=false;

  this.userService.getCurrentUserDetails().subscribe(user => this.employee_full_name=user.first_name+' '+user.last_name)

  



   
  }
  

  getWeekly() {
    if (this.filters==undefined) return;
    this.week=this.getWeek(this.current_date);
    this.timesheetService.getWeeklyImputations(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe(data => this.weekly=data)
  }

  getWeek(fromDate : Date){
    
    const first = fromDate.getDate() - fromDate.getDay() + 1;
  
    const monday = new Date(fromDate.setDate(first));
    monday.setHours(0,0,0,0)
    var sunday = new Date(fromDate.setDate(fromDate.getDate()-fromDate.getDay()))
    sunday.setHours(0,0,0,0)
    var result = [new Date(monday)];
    while (monday.setDate(monday.getDate()+1) && monday.getDay()!==1) {
     result.push(new Date(monday));
    }

   
    return result;
   }
   nextWeek() : Date {

     this.current_date= new Date(this.current_date.getFullYear(), this.current_date.getMonth(), this.current_date.getDate()+7); 

     this.loadData()
     this.getWeekly()

     return this.current_date
   }
   previousWeek() : Date {

    this.current_date= new Date(this.current_date.getFullYear(), this.current_date.getMonth(), this.current_date.getDate()-7);
    this.loadData()
    this.getWeekly()

    return this.current_date
  }
  currentWeek() : Date {
    this.current_date=this.CURRENT_DATE
    this.loadData()
    this.getWeekly()

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

  getTotals() : number[] {
    let sums : number[]= [0,0,0,0,0,0,0,0]
    this.dataSource2.forEach((row : TableElement | Break | Group)=> {
      if (!("isLeave" in row) && !("isGroup" in row)) {
        //weird behavior, workload is number but is treated as string
        sums[0] +=parseInt((row.monday.value?.workload || 0).toString())
        sums[1] += parseInt((row.tuesday.value?.workload || 0).toString())
        sums[2] += parseInt((row.wednesday.value?.workload || 0).toString())
        sums[3] += parseInt((row.thursday.value?.workload || 0).toString())
        sums[4] += parseInt((row.friday.value?.workload || 0).toString())
      }  
    })
  
    sums[7]=sums[0]+sums[1]+sums[2]+sums[3]+sums[4]
    return sums
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
       // this.getWeeklyImputations()
      });
   
      }
    
      
      
    //** THE MULTI SELECT CODE HERE **


      toggleSelected(obj :Cell , event : any) {
          if (event.shiftKey) {
          obj.selected = !obj.selected;
          if(obj.selected && !this.previousSelected.includes(obj)) { this.previousSelected.push(obj)}
    

    
        } else if (event.ctrlKey) {
          obj.selected = !obj.selected;
          if(obj.selected && !this.previousSelected.includes(obj)) { this.previousSelected.push(obj)}

        } else {
            //set all previous cells to selected = false;
            this.dataSource2.forEach((row :any) => {
              if (!row?.isGroup && !row?.isLeave) {
                  row.monday.selected=false
                  row.tuesday.selected=false
                  row.wednesday.selected=false
                  row.thursday.selected=false
                  row.friday.selected=false

              }
              
            })
            obj.selected = !obj.selected;
            this.previousSelected=[]
            this.previousSelected.push(obj);
        }

      }
      //TODO : fix solution : we create cells automatically , remove fillCells()
      fillCells(obj : Task, date : Date , event : any, index ?: number) {
         //let  date_index = obj.id.split('-')[0]
        this.previousSelected.forEach((elt) => {
          if (elt.value!=undefined) {
            elt.value.workload=event.target.value
          }
          else if(elt.value===undefined || elt.value=="") {
            let date_index = parseInt(elt.id.split('-')[1])-1;
            let task_id = parseInt(elt.id.split('-')[0]);

            let task  = this.tasks.find((t : any) => {
              return t.task_id === task_id;
            })
           
           elt.value= new Imputation(task!,this.convertDateToText(this.week[date_index]),event.target.value)
          }
          elt.touched=true
          if(this.touchedCells.indexOf(elt)==-1) this.touchedCells.push(elt)
          })
        }

        enableEdit() {
          this.toggleButton=!this.toggleButton;
          if (this.toggleButton) this.editButtonValue="cancel";
          else { this.editButtonValue="edit";
          this.touchedCells=[]
        }
          
        }

        saveChanges() {
          //TODO : something needs to be done , sub array calculates total from input , should only be for submitted(selected) input 
          const subarray = this.getTotals().slice(0,5)
          if(subarray[0]+this.break.monday>8 || subarray[1]+this.break.tuesday>8 || subarray[2]+this.break.wednesday>8 ||subarray[3]+this.break.thursday>8 ||subarray[4]+this.break.friday>8 )
          {
            this.handleError("you surpassed work hours limit per day!");

            this.touchedCells=[]
            this.previousSelected= []
            this.editButtonValue="edit";
            this.toggleButton=false;
             return;
          }
        


              this.touchedCells.forEach((cell) =>{
                var imputationForm= this.formBuilder.group({
                  'name': [cell.value?.task , [Validators.required]],
                  'day': [cell.value?.day ,[Validators.required]],
                  'workload': [cell.value?.workload ,[Validators.required,Validators.max(60*8),Validators.min(30)]],
                  'comment': ['']
                  
                });
                //check totals 1st
               
                this.timesheetService.postImputation(imputationForm).subscribe(
                  {
                    complete: () => {this.handleGoodResponse('post'); }, // completeHandler
                    error: (err) => { 
                      this.handleError("an error occured"); },    // errorHandler 
                    next: () => {  }     // nextHandler
                  }
                )
            
          }) 
                                      
          
    
    this.touchedCells=[]
    this.previousSelected= []
    this.editButtonValue="edit";
    this.toggleButton=false;
  }

      addTask(task : any) {
        var new_task  = new Task(task.task_id,task.task_name,task.project_id)
        var task_id = new_task.id
        var new_row = {task: new_task, monday:new Cell(task_id+'-'+1,undefined),tuesday :new Cell(task_id+'-'+2,undefined),wednesday:new Cell(task_id+'-'+3,undefined),thursday :new Cell(task_id+'-'+4,undefined),friday :new Cell(task_id+'-'+5,undefined),saturday:new Cell(task_id+'-'+6,undefined),sunday:new Cell(task_id+'-'+7,undefined),total:0}
        var group_index = this.dataSource2.findIndex((object : any) => {return (object.project==task.project_name && object.isGroup==true)})
        if (group_index!==-1)
        this.dataSource2.splice(group_index+1, 0, new_row);
        else this.dataSource2.push(new_row)
        this.dataSource2 = [...this.dataSource2]

      }
      exists(task : any) : boolean {
        const found = this.dataSource2.find((x : any) => {return (task.task_id ==x?.task?.id && task.task_name ==x?.task?.name)})
        if (found) return true;
        return false
      }

       async printWeeklyPreview() {     
        
        //TODO : fetch file from server and read it!!

        //step 1 : load file
        const filePath: string = `assets/week.xlsx`;
        const buffer = await fetch(filePath).then(res => res.arrayBuffer());
        const workbook = new Excel.Workbook;
        await workbook.xlsx.load(buffer);
        const worksheet = workbook.getWorksheet("Feuille Temps");
        
        //THIS WORKS!!

       //step 2 : load and insert data

       let project_row_index=17;
       //project rows : from 17 -> to 22 , from B to J


       this.allProjects.forEach(project => {
        
        let relatedImputations = this.weekly.filter(imputation => imputation.project_id==project.project_id)
        let project_row : any[] = [0,0,0,0,0,0,0,0] //row that we have to insert in the worksheet
        let monday_workkload = relatedImputations.filter(imputation => imputation.dow.trim()=='monday').map(imputation => imputation.workload/8).reduce((partialSum, a) => partialSum + a, 0);
        let tuesday_workload = relatedImputations.filter(imputation => imputation.dow.trim()=='tuesday').map(imputation => imputation.workload/8).reduce((partialSum, a) => partialSum + a, 0);
        let wednesday_workload = relatedImputations.filter(imputation => imputation.dow.trim()=='wednesday').map(imputation => imputation.workload/8).reduce((partialSum, a) => partialSum + a, 0);
        let thursday_workload = relatedImputations.filter(imputation => imputation.dow.trim()=='thursday').map(imputation => imputation.workload/8).reduce((partialSum, a) => partialSum + a, 0);
        let firday_workload = relatedImputations.filter(imputation => imputation.dow.trim()=='friday').map(imputation => imputation.workload/8).reduce((partialSum, a) => partialSum + a, 0);

          //create row containing imputations per week
        let total=monday_workkload+tuesday_workload+wednesday_workload+thursday_workload+firday_workload
        project_row = [project.project_name,monday_workkload,tuesday_workload,wednesday_workload,thursday_workload,firday_workload,0,0,total]
        
        if (project_row[8]!==0){ //if total of row is different than 0, we insert it in the document
          let project_row_string = project_row_index.toString();
          worksheet.getCell('B'.concat(project_row_string)).value = project_row[0];
          worksheet.getCell('C'.concat(project_row_string)).value = project_row[1]
          worksheet.getCell('D'.concat(project_row_string)).value = project_row[2]
          worksheet.getCell('E'.concat(project_row_string)).value = project_row[3]
          worksheet.getCell('F'.concat(project_row_string)).value = project_row[4]
          worksheet.getCell('G'.concat(project_row_string)).value = project_row[5]
          worksheet.getCell('H'.concat(project_row_string)).value = project_row[6]
          worksheet.getCell('I'.concat(project_row_string)).value = project_row[7]
          worksheet.getCell('J'.concat(project_row_string)).value = project_row[8]
        }

          project_row_index++;

       })
       //insert holidays and congés
        console.log(this.break)
        console.log(this.holidays)
        if (this.holidays?.includes('monday'))  worksheet.getCell('C23').value=1
        else worksheet.getCell('C23').value= (this.break.monday/8) || null 
        if (this.holidays?.includes('tuesday'))  worksheet.getCell('D23').value=1
        else worksheet.getCell('D23').value= (this.break.tuesday/8) || null 
        if (this.holidays?.includes('wednesday'))  worksheet.getCell('E23').value=1
        else worksheet.getCell('E23').value= (this.break.wednesday/8) || null 
        if (this.holidays?.includes('thursday'))  worksheet.getCell('F23').value=1
        else worksheet.getCell('F23').value= (this.break.thursday/8) || null 
        if (this.holidays?.includes('friday'))  worksheet.getCell('G23').value=1
        else worksheet.getCell('G23').value= (this.break.friday/8) || null 


      
       //insert other userful information like date, names,...
      let now = this.current_date;
      let onejan = new Date(now.getFullYear(), 0, 1);
      let week_number = Math.ceil((((now.getTime() - onejan.getTime()) / 86400000) + onejan.getDay() + 1) / 7);
      const month = now.toLocaleString('default', { month: 'long' });
      worksheet.getCell('C12').value = 'W'+week_number
      worksheet.getCell('H12').value= month
      worksheet.getCell('C10').value=this.employee_full_name





        //step 3 : save changes into new doc
        workbook.xlsx.writeBuffer().then(data => {
          const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' }); 
          FileSaver.saveAs(blob, `${this.employee_full_name}-W${week_number}.xlsx`);
         })
           
      }

     
  

     
      loadData() {
        if (this.filters==undefined) return;
        this.disabled=true;
        this.week=this.getWeek(this.current_date);
        this.timesheetService.getLeaves(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe((data : Break) => {
          this.break=data
       } )
       this.getHolidays()
        this.timesheetService.loadData(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe((data : any) => {
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
        
              this.dataSource2 = []
              
        
              this.getTasks()
              

             
              for (let p of this.filters.controls['projects']!.value) {
                
                let tasks = this.fetchRelatedTasks(p) //this returns list of tasks for specific project p
                if (tasks.length!=0) {
                  //create table rows from those tasks
                  const group : Group ={project : p.project_name, isGroup: true}
                  this.dataSource2.push(group)
                  tasks.forEach((task : Task) => {
                    let id = task.id
                    this.dataSource2.push({task : task, monday : new Cell(id+'-'+1,undefined), tuesday :new Cell(id+'-'+2,undefined), wednesday : new Cell(id+'-'+3,undefined), thursday : new Cell(id+'-'+4,undefined), 
                    friday : new Cell(id+'-'+5,undefined), saturday : new Cell(id+'-'+6,undefined) , sunday : new Cell(id+'-'+0,undefined), total :0})
                  })
                }

        
        
              
             }
              this.populateRows(this.dataSource2)
            
              //this.dataSource2.unshift()
              this.dataSource2 = [this.break,...this.dataSource2]
              this.totals = this.getTotals()


      })
     
      this.disabled=false
    }

 
    fetchRelatedTasks(p : any) : Task[] {
    //  let result;
      let result = []
      for (let task of this.tasknames) {
        if (task.project_id==p.project_id) result.push(task);
    
    }
    return result
     
  }
  
    getTasks()  {

        this.tasknames.forEach((task : Task)=>
        {
         let found = this.filters?.controls['tasks'].value.find((t : any) => t.task_id==task.id )
         task.project_id=found?.project_id
        })
      
     
    }

    populateRows(data : any[])  {
        for (let row of data) {
          if (!row.isGoupBy) {
              let row_sum=0;
              for(let i=0;i<7;i++){
                let day =this.convertDateToText(this.week[i])
                const found = this.imputations.find(element => (element.task.id==row?.task?.id && element.day==day))
                if(found) {
                  row_sum+=found.workload
                  //TODO : make week[0] = monday and week[6] = sunday!!!!
                  switch(i) {
                    case 0 : row.monday.value = found
                    break;
                    case 1 : row.tuesday.value = found
                    break;
                    case 2 : row.wednesday.value = found
                    break;
                    case 3 : row.thursday.value = found
                    break;
                    case 4 : row.friday.value = found
                    break;
                    case 5 : row.saturday.value = found
                    break;
                    case 6 : row.sunday.value = found
                    break;
                  }
                }
              }
             row.total = row_sum
        
          }
        }
    }

    getLeaves()  {
      this.timesheetService.getLeaves(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe((data : Break) => {
         this.break=data
      } )
    }
    //TODO : integrate holidays and Leaves in the same function
    getHolidays() {
      this.timesheetService.getHolidays(this.convertDateToText(this.week[0]),this.convertDateToText(this.week[6])).subscribe(data => this.holidays=data)
    }

    //ALERT SERVICE 
    handleGoodResponse(error : string) {
      this.displaySuccess(error);
    }
    handleError(err: string) {
      this.displayError(err);
    }
    private displayError(message: string) {
      this.alertService.error(message,
        { autoClose: true }
      );
    }
  
    private displaySuccess(event : string) {
      this.alertService.success("Imputation successfully updated!",
        { autoClose: true }
      );
      
    }

    

    
  }

