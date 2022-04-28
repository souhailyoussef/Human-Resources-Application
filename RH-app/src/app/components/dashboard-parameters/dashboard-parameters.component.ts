import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NodeService } from 'src/app/services/node.service';
import {NestedTreeControl} from '@angular/cdk/tree';
import {MatTree, MatTreeNestedDataSource} from '@angular/material/tree';
import {MatTreeFlatDataSource, MatTreeFlattener} from '@angular/material/tree';
import {FlatTreeControl} from '@angular/cdk/tree';
import { Rubrique } from 'src/app/rubrique';
import { nestedData } from 'src/assets/nestedData';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { DialogBoxComponent } from '../dialog-box/dialog-box.component';
import { BehaviorSubject } from 'rxjs';
import { MatTooltipDefaultOptions, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material/tooltip';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from 'src/environments/environment';
import { file } from 'jszip';
import { FileService } from 'src/app/services/file.service';
import { saveAs } from 'file-saver';
import * as fileSaver from 'file-saver';
import { AlertService } from 'src/app/modules/alert/alert.service';





interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  value: number;
  level: number;
  id: number;
  file_id: number;
}

export const myCustomTooltipDefaults: MatTooltipDefaultOptions = {
  showDelay: 500,
  hideDelay: 500,
  touchendHideDelay: 500,
};



@Component({
  selector: 'app-dashboard-parameters',
  templateUrl: './dashboard-parameters.component.html',
  styleUrls: ['./dashboard-parameters.component.css'],
  providers :[{provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: myCustomTooltipDefaults}],
  
})

export class DashboardParametersComponent implements OnInit {
 
  
  displayedColumns: string[] = ['rubrique', 'actions','script'];

  parent_name :  string;
  value : number;
  newName: string;
  newParent : string;
  searchText = '';
  files : any[] = [];
  error_message = "An error occured"



  

  private transformer = (node: Rubrique) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      value: node.value,
      level: node.depth,
      id: node.id,
      file_id : node.file_id
    };
  }

  treeControl : any = new FlatTreeControl<ExampleFlatNode>(
      node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(
      this.transformer, node => node.level, 
      node => node.expandable, node => node.children);

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
 
  constructor(private nodeService : NodeService,private matDialog: MatDialog, 
    private fileService : FileService,private alertService: AlertService) { 
    

    this.loadContent();
    this.fileService.getFiles().subscribe(data => this.files);
  }


  


  ngOnInit(): void {

    
    this.nodeService.refreshNeeded$.subscribe(() => this.loadContent())
    this.fileService.refreshNeeded$.subscribe(()=> this.loadContent())
  
  }
  hasChild = (_: number, node: Rubrique) => !!node.children && node.children.length > 0;

  saveNode(parent_id : number , name : string , value ?: number) {
    this.alertService.clear();
    this.nodeService.saveNode(parent_id,name,value).subscribe({
      complete: () => {this.handleGoodResponse('add'); }, // completeHandler
      error: (err) => {  this.handleError(err)},    // errorHandler 
      next: () => {  }     // nextHandler
    });
  }

  deleteNode(id : number) {
    this.alertService.clear();
    this.nodeService.deleteNode(id).subscribe(
      {
        complete: () => { this.handleGoodResponse('delete');}, // completeHandler
        error: () => {  this.handleError(this.error_message)},    // errorHandler 
        next: () => {  }     // nextHandler
      }
    );
  }

  updateNodeName(id : number, newName : string) {
    this.alertService.clear();
    this.nodeService.changeNodeName(id,newName).subscribe(
      {
        complete: () => { this.handleGoodResponse('edit');
         }, // completeHandler
        error: () => {  this.handleError(this.error_message)},    // errorHandler 
        next: () => {  }     // nextHandler
      }
    );
  }

  private loadContent() {
    this.nodeService.getData().subscribe(
      {
        
        // completeHandler
        error: () => {  this.handleError(this.error_message)},    // errorHandler 
        next: (data : any) => { 
           this.dataSource.data = [data] }     // nextHandler
      }
      
)
    this.fileService.getFiles().subscribe((data : any) => this.files = data)
  }

  filterScriptName(rubrique_id : number) : string{
    var script_name
    try {
       script_name = this.files.find((script : any) => script.rubrique_id===rubrique_id)?.name ;
       if (script_name==null || undefined) {
         return ''
       }
       return script_name;
    }
    catch (error) {
      console.error(error)
      return 'no script available'
    }
 
    
  }




  openDialogBox(event: string, node : Rubrique) {
    let dialogRef =  this.matDialog.open(DialogBoxComponent,{
      data: {event,node},
      height:'200px',
      width: '400px',
      maxHeight:'600px',
      disableClose:true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result!=null) {
  
        if(result.event === 'add') {
          this.saveNode(result.node.id,result.nodename);
        }
        else if(result.event === 'delete') {
          this.deleteNode(result.id);
        }
        else if(result.event === 'edit') {
          this.updateNodeName(result.id,result.newName);
        }
        console.log(result);
      }
     

    })
  }


  filterParentNode(node: ExampleFlatNode): boolean {

    if (
      !this.searchText ||
      node.name.toLowerCase().indexOf(this.searchText?.toLowerCase()) !==-1
    ) {
      return false
    }
    const descendants = this.treeControl.getDescendants(node)

    if (
      descendants.some(
        (descendantNode : ExampleFlatNode) =>
          descendantNode.name
            .toLowerCase()
            .indexOf(this.searchText?.toLowerCase()) !== -1
      )
    ) {
      return false
    }

    return true
  }
  // search filter logic end
  fileName = '';
  id = 1;
 


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

    handleGoodResponse(error : string) {
      this.displaySuccess(error);
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
      if (event==="add") {
        this.alertService.success("Element successfully added!",
        { autoClose: false }
      );
      }
      else if (event==="delete") {
        this.alertService.success("Element successfully deleted!",
        { autoClose: false }
      );
      
      }
      else if (event==="edit") {
        this.alertService.success("Element successfully edited!",
        { autoClose: false }
      );
      }
      else if (event==="upload") {
        this.alertService.success("script successfully uploaded!",
        { autoClose: false }
      );
      }
      
    }

  
}


