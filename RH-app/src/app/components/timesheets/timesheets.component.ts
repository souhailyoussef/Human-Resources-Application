import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatOption } from '@angular/material/core';
import { MatSelect } from '@angular/material/select';
import { Observer } from 'rxjs';
import { Observable, Subject } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { WeeklyTimesheetComponent } from '../weekly-timesheet/weekly-timesheet.component';




export class User {
  constructor(
    public name: string,
    public selected: boolean = false
  ) {
    if (selected === undefined) selected = false;
  }
}

export class Project {
  project_id: number
  project_name : string;
  project_status : string;
  project_description: string;
  selected : boolean;
  constructor(project_id : number,project_name : string,project_status : string,project_description : string) {
    this.project_id=project_id
    this.project_name=project_name
    this.project_status=project_status
    this.project_description=project_description
    this.selected=false

  }
}
export class Task {
  task_id: number
  task_name : string;
  tag : string;
  project_id: number;
  project_name : string;
  selected : boolean;
  constructor(task_id : number,task_name : string,tag : string,project_id : number,project_name: string) {
    this.task_id=task_id
    this.task_name=task_name
    this.tag=tag
    this.project_id=project_id
    this.project_name= project_name
    this.selected=false
  }
}

@Component({
  selector: 'app-timesheets',
  templateUrl: './timesheets.component.html',
  styleUrls: ['./timesheets.component.css'],
  
})
export class TimesheetsComponent implements OnInit {

  viewDate: Date = new Date();
  closeResult = '';
  @ViewChild('allSelected') private allSelected: MatOption;


  /* */ 


  tasks : Task[]  = []
  displayedTasks : Task[] = []
  loading : boolean = false;
  date_from : Date = new Date();
  date_to : Date = new Date();
  projectsAndTasks : any;
  projects :  Project[] = []
  CURRENT_DATE= new Date()
  tasksFormControl = new FormControl();
  projectsControlForm = new FormControl();
  groupByFormControl = new FormControl('task');

  filterForm : FormGroup ; 

  

  selectAllTasks(ev : any){
   
    if(ev._selected){
    this.tasksFormControl.setValue(this.filterTasks());
    ev._selected=true;
    }
    if(ev._selected==false){
      this.tasksFormControl.setValue([]);
    }
    
  }
  selectAllProjects(ev : any){
   
    if(ev._selected){
this.projectsControlForm.setValue(this.projects);
ev._selected=true;
    }
    if(ev._selected==false){
      this.projectsControlForm.setValue([]);
    }
    
  }

  constructor(private userService : UserService, private formBuilder : FormBuilder) {

    this.filterForm = this.formBuilder.group({
      'projects' : this.projectsControlForm,
      'tasks' : [this.tasks,this.tasksFormControl],
      'group-by' : this.groupByFormControl

  })
}

  ngOnInit(): void {
    
    this.userService.getCurrentTasksAndProjects(8,"2022-01-01").subscribe( 
      res => {
        this.projectsAndTasks=res
        //load all projects and tasks 
        for (let x of this.projectsAndTasks) {
            this.projects.push(new Project(x.project_id,x.project_name,x.project_status,x.project_description))
            this.tasks.push(new Task(x.task_id,x.task_name,x.tag,x.project_id,x.project_name))
            this.displayedTasks=this.tasks
        }
      })
      this.projectsControlForm.setValue(this.projects)
      this.tasksFormControl.setValue(this.tasks)

      this.projectsControlForm.valueChanges.subscribe(data => {
        this.displayedTasks = this.filterTasks()
        console.log(this.filterForm.value)
      })
  
     
  }

  filterTasks() : Task[] {
    var project_ids : number[] =[]
    if (this.projectsControlForm.value!=null){
       project_ids = this.projectsControlForm.value.map( (project : Project)=> {
        return project.project_id
      })
    }
    const result = this.tasks.filter( task => project_ids.indexOf(task.project_id)!=-1) 
    return result
  }
  
  loadData() {
    //fire event to child component to load data !!
    this.loading=!this.loading;
  
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
  
 
  
  
 


}
