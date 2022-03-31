import { computeMsgId } from '@angular/compiler';
import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Console } from 'console';
import * as fileSaver from 'file-saver';
import { BehaviorSubject, switchMap } from 'rxjs';
import { Observable } from 'rxjs';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { Rubrique } from 'src/app/rubrique';
import { FileService } from 'src/app/services/file.service';




@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent implements OnInit {

  nodename : string;
  newName : string;
  uploadSuccessful : boolean ;



  constructor(@Inject(MAT_DIALOG_DATA) public data : {event: string, node : Rubrique},
  private matDialogRef : MatDialogRef<DialogBoxComponent>,private fileService : FileService, private alertService : AlertService) 
  {
    this.newName = data.node.name ;
  }
    //@Optional() is used to prevent error if no data is passed


  ngOnInit(): void {
    this.fileService.refreshNeeded$.subscribe(()=> this.closeDialogBox());
    this.uploadSuccessful=false;
    

     
  }
  
 ngOnDestroy() {
   // if conditions depending on event
 }

 confirmChanges() {
   if (this.data.event==='add' && this.nodename.trim().length >=3 ) {
    this.matDialogRef.close({event: this.data.event, node : this.data.node, nodename : this.nodename.trim()});
   }
   else if (this.data.event==='delete') {
    this.matDialogRef.close({ event: this.data.event, id : this.data.node.id});
   }
   else if (this.data.event ==='edit' && this.newName.trim().length >=3 ) {
    this.matDialogRef.close({event: this.data.event, id: this.data.node.id , newName : this.newName.trim()});
   }
   else if (this.data.event==='upload') {
     
      this.uploadFile(this.data.node.id,this.data.node.name);
      
     
   }
   //error handling needed

 }

 closeDialogBox() {
   this.matDialogRef.close();
 }
 fileName = '';
 file:File;
  error : any;
 onFileSelected(event :any) {

     this.file = event.target.files[0];
       
    }
  uploadFile(id : number, rubrique_name : string) {
    let fileName = rubrique_name+'.'+'groovy';
     if (this.file) {
         this.fileName = this.file.name;
           this.fileService.uploadFile(id,this.file,fileName).subscribe({
            complete: () => { this.handleGoodResponse("upload")}, // completeHandler
            error: (err) => { this.handleError(err)},    // errorHandler 
            next: () => {  }     // nextHandler
          });
        
           

     }

     




}



  downloadFile(id:number,script_name:string) {
    if (script_name=='no script available') {
      console.log("nothing")
    }
    else {
      this.fileService.downloadFile(id).subscribe((response: any) => { //when you use stricter type checking
        let blob:any = new Blob([response], { type: 'text/json; charset=utf-8' });
        const url = window.URL.createObjectURL(blob);
        //window.open(url);
        //window.location.href = response.url;
        fileSaver.saveAs(blob, script_name);
      //}), error => console.log('Error downloading the file'),
      }), (error: any) => console.log('Error downloading the file'), //when you use stricter type checking
                   () => console.info('File downloaded successfully');
    }
    }

    handleGoodResponse(msg : string) {
      this.displaySuccess(msg);
    }
    handleError(err: string) {
      this.displayError(err);
    }
    private displayError(message: string) {
      this.alertService.error(message,
        { autoClose: false }
      );
    }
  
    private displaySuccess(event : string) {
      
       if (event==="upload") {
        this.alertService.success("Script successfully uploaded!",
        { autoClose: false }
      );
      }

}
}
