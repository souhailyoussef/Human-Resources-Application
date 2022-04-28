import { FlatTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { NumericDictionaryIteratee } from 'lodash';
import { Rubrique } from 'src/app/rubrique';
import { NodeService } from 'src/app/services/node.service';
import { DashboardService } from '../dashboard.service';


interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  count: number;
  level: number;
  parameters: string;
  value:number;
  january:number;
  february:number;
  march:number;
  april:number;
  may:number;
  june:number;
  july:number;
  august:number;
  september:number;
  october:number;
  november:number;
  december:number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  info : {
    clients : number,
    projects : number,
    employees : number
  } 
  displayedColumns: string[] = ['rubrique', 'parameters','value','january'  ,'february','march','april','may','june','july','august','september','october','november','december'];


  

  private transformer = (node: Rubrique) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      value: node.value,
      level: node.depth,
      id: node.id,
      parameters: node.parameters,
      january:node.january,
      february:node.february,
      march:node.march,
      april:node.april,
      may:node.may,
      june:node.june,
      july:node.july,
      august:node.august,
      september:node.september,
      october:node.october,
      november:node.november,
      december:node.december
    };
  }

  treeControl = new FlatTreeControl<ExampleFlatNode>(
      node => node.level, node => node.expandable);

  treeFlattener : any = new MatTreeFlattener(
      this.transformer, node => node.level, 
      node => node.expandable, node => node.children);

  dataSource = new MatTreeFlatDataSource(this.treeControl , this.treeFlattener);

  constructor(private dashboardService : DashboardService, private nodeService : NodeService) {
    this.loadContent();

   }

  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;
  ngOnInit(): void {
    this.nodeService.refreshNeeded$.subscribe(() => this.loadContent())

    this.dashboardService.getGeneralInfo().subscribe( data => {
      this.info = data;

    })
  }

  private loadContent() {
    this.nodeService.getData().subscribe(
      {
        
        next: (data : any) => { 
           this.dataSource.data = [data] 
          }     // nextHandler
      })

}





}
