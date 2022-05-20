import { ElementRef, ViewChild } from '@angular/core';
import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { map, Observable, startWith } from 'rxjs';
import { TaskService } from 'src/app/services/task.service';
import { Task } from 'src/app/task';




@Component({
  selector: 'app-task-modal',
  templateUrl: './task-modal.component.html',
  styleUrls: ['./task-modal.component.css']
})
export class TaskModalComponent implements OnInit {
  
  @ViewChild('dateinput')  dateinput!: ElementRef; 
  imputationForm : FormGroup; //our form
  tasks : Task[] = [];
  workload : number
  submitted: boolean= false;
  control = new FormControl();
  dateControl = new FormControl()
  currentDate = new Date().toISOString().slice(0,10);

   constructor(public dialogRef: MatDialogRef<TaskModalComponent>,@Inject(MAT_DIALOG_DATA) public data : any,
  private formBuilder: FormBuilder,private taskService : TaskService ) {
    
    this.imputationForm= this.formBuilder.group({
      'name': ['', [Validators.required]],
      'day': ['',[Validators.required]],
      'workload': ['',[Validators.required,Validators.max(60*8),Validators.min(30)]],
      'comment': [''],
      'status':['']
    });
  }
ngAfterViewInit() {
  if (this.data.event=='edit')
  (this.dateinput.nativeElement.disabled=true)

}

  ngOnInit(): void {
    if (this.data.event=='add') {
      this.taskService.getTask(this.data.task).subscribe((res  : any)=> {
        let current_task  =res
        this.tasks = [current_task]
        this.imputationForm.controls['name'].setValue(current_task)
      })
    }
    else if (this.data.event=='edit') {
      this.tasks = [this.data.imputation.task]
      this.workload=this.data.imputation.workload
      this.imputationForm.controls['comment'].setValue(this.data.imputation.comment)
      this.imputationForm.controls['day'].setValue(this.data.imputation.day)
    }
    
}

  onClose(): void {
    this.dialogRef.close();
  }
  onSubmit() {
    console.log(this.imputationForm.value)
    this.submitted=true
    if (this.imputationForm.valid) {
      console.log("valid form")
      this.taskService.postImputation(this.imputationForm).subscribe()
    }
  }

  
 
  convertTime(workload : number |undefined ) : string {
      var text = "";
      if (workload==undefined) {
        return ""
      }

      let hours,minutes;
      hours = Math.floor(workload/60);
      minutes = Math.floor(workload % 60);

      if (hours==0) {
        text=`${minutes} minute(s)`
      }
      else if (hours!=0 && minutes==0) {
        text=`${hours} hour(s)`
      }
      else if(hours!=0 && minutes !=0) {
        text=`${hours} hour(s) and ${minutes} minute(s)`
      }
      return text
    
  
  }
  
}