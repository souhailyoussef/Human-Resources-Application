import { Component, OnInit, ViewChild } from '@angular/core';
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





interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  value: number;
  level: number;
  id: number;
}

export const myCustomTooltipDefaults: MatTooltipDefaultOptions = {
  showDelay: 1000,
  hideDelay: 1000,
  touchendHideDelay: 1000,
};

@Component({
  selector: 'app-dashboard-parameters',
  templateUrl: './dashboard-parameters.component.html',
  styleUrls: ['./dashboard-parameters.component.css'],
  providers :[{provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: myCustomTooltipDefaults}]
  
})

export class DashboardParametersComponent implements OnInit {
 
  
  displayedColumns: string[] = ['rubrique', 'edit'];

  parent_name :  string;
  value : number;
  newName: string;
  newParent : string;
  searchText = '';



  

  private transformer = (node: Rubrique) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      value: node.value,
      level: node.depth,
      id: node.id
    };
  }

  treeControl : any = new FlatTreeControl<ExampleFlatNode>(
      node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(
      this.transformer, node => node.level, 
      node => node.expandable, node => node.children);

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
 
  constructor(private nodeService : NodeService,private matDialog: MatDialog) { 
    
    this.loadContent();
  }


  


  ngOnInit(): void {
    
    this.nodeService.refreshNeeded$.subscribe(() => this.loadContent())
  
  }
  hasChild = (_: number, node: Rubrique) => !!node.children && node.children.length > 0;

  saveNode(parent_id : number , name : string , value ?: number) {
    this.nodeService.saveNode(parent_id,name,value).subscribe();
  }

  deleteNode(id : number) {
    this.nodeService.deleteNode(id).subscribe();
  }

  updateNodeName(id : number, newName : string) {
    this.nodeService.changeNodeName(id,newName).subscribe();
  }

  private loadContent() {
    this.nodeService.getData().subscribe((data :any) => this.dataSource.data = [data]
)
  }




  openDialogBox(event: string, node : Rubrique) {
    let dialogRef =  this.matDialog.open(DialogBoxComponent,{
      data: {event,node},
      height:'200px',
      width: '400px',
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

  


  
}


