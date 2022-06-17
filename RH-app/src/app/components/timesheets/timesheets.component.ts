import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Client } from 'src/app/client';

import { timesheetService } from 'src/app/services/timesheet.service';
import { UserService } from 'src/app/services/user.service';






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
    this.selected=true

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



interface TaskGroup {
  disabled? : boolean;
  project : Project;
  tasks : Task[];
}

@Component({
  selector: 'app-timesheets',
  templateUrl: './timesheets.component.html',
  styleUrls: ['./timesheets.component.css'],
  
})
export class TimesheetsComponent implements OnInit {

  
///////////////////////////
  viewDate: Date = new Date();
  closeResult = '';


  /* */ 


  tasks : Task[]  = []
  loading : boolean = false;
  date : Date = new Date();
  projectsAndTasks : any;
  projects :  Project[] = []
  CURRENT_DATE= new Date()
  tasksFormControl = new FormControl();
  projectsControlForm = new FormControl();
  groupByFormControl = new FormControl('project');
  dateFormControl = new FormControl()
  filterForm : FormGroup ; 
  taskGroups : TaskGroup[] = []
  outputData : FormGroup; //this data will be passed to weekly-timesheet on btn click
 
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
      //we select all projects
      this.projects.forEach(project => project.selected=true)
      ev._selected=true;
    }
    if(ev._selected==false){
      this.projects.forEach(project => project.selected=false)
      //this.projectsControlForm.setValue([]);
    }

    
  }

  constructor(private userService : UserService, private formBuilder : FormBuilder, private timesheetService : timesheetService) {

    this.filterForm = this.formBuilder.group({
      'projects' : this.projectsControlForm,
      'tasks' : this.tasksFormControl,
      'group-by' : this.groupByFormControl,
      'date' : this.dateFormControl

  })
}

  ngOnInit(): void {
    
    this.userService.getCurrentTasksAndProjects(8,"2022-01-01").subscribe( 
      res => {
        this.projectsAndTasks=res
        //load all projects and tasks 
        for (let x of this.projectsAndTasks) {
            this.projects.push(new Project(x.project_id,x.project_name,x.project_status,x.project_description))
            
            this.tasks.push(new Task(x.task_id,x.task_name,x.task_tag,x.project_id,x.project_name))
          
        }
        
        this.projects = this.projects.filter((value, index) => {
          const _value = JSON.stringify(value);
          return index === this.projects.findIndex(obj => {
            return JSON.stringify(obj) === _value;
          });
        });
        this.projects.forEach((project)=> {
          let relatedTasks  = this.tasks.filter((task) => task.project_id==project.project_id) 
          this.taskGroups.push({
            project: project,
            tasks : relatedTasks
          })  
        } )

        this.projectsControlForm.setValue(this.projects)
        this.tasksFormControl.setValue(this.tasks)
        this.dateFormControl.setValue(this.date)
      })
      
     
     
  }
  filterTasks() : TaskGroup[] {
    var project_ids : number[] =[]
    if (this.projectsControlForm.value!=null){
       project_ids = this.projectsControlForm.value.map( (project : Project)=> {
        return project.project_id
      })
    }
    const result = this.taskGroups.filter ( group => project_ids.indexOf(group.project.project_id)!=-1)

    return result

  }

  ngAfterViewInit() {
    this.filterForm.valueChanges.subscribe((data) => {
      this.timesheetService.updateFilter(this.filterForm);
    })
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
