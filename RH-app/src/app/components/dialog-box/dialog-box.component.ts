import { Component, Inject, OnInit, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Rubrique } from 'src/app/rubrique';




@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent implements OnInit {

  nodename : string;
  newName : string;

  constructor(@Inject(MAT_DIALOG_DATA) public data : {event: string, node : Rubrique},
  private matDialogRef : MatDialogRef<DialogBoxComponent>) 
  {}
    //@Optional() is used to prevent error if no data is passed
    

  ngOnInit(): void {
  }
  
 ngOnDestroy() {
   // if conditions depending on event
 }
 confirmChanges() {
   if (this.data.event==='add') {
    this.matDialogRef.close({event: this.data.event, node : this.data.node, nodename : this.nodename});
   }
   else if (this.data.event==='delete') {
    this.matDialogRef.close({ event: this.data.event, id : this.data.node.id});
   }
   else if (this.data.event ==='edit') {
    this.matDialogRef.close({event: this.data.event, id: this.data.node.id , newName : this.newName});
   }
   else this.matDialogRef.close();
   //error handling needed

 }

 closeDialogBox() {
   this.matDialogRef.close();
 }
}
