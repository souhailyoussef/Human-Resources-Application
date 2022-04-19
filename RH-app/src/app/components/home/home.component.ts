import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { first } from 'rxjs';
import { LoginuserService } from 'src/app/services/loginuser.service';
import { User } from 'src/app/user';
import * as Highcharts from 'highcharts';
import { NgChartsModule } from 'ng2-charts';
import { UserService } from 'src/app/services/user.service';



export interface Tile {
  cols: number;
  rows: number;
  text: string;
}


export interface PeriodicElement {
  name: string;
  id: number;
  team: number;
  date: string;
  status:string
}

export interface Note {
  title : string;
  start_time: Date;
  end_time:Date;
  color: string;
}
export interface Repartition {
  female : number;
  male : number;
  total : number;
}


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  displayedColumns: string[] = ['id', 'name', 'team', 'date','status'];
  current_date : Date = new Date();

   ELEMENT_DATA: PeriodicElement[] = [
    {id: 1, name: 'Katrina Benett', team: 1, date: '29/05/2022',status:'pending'},
    {id: 2, name: 'Pawel Dennis', team: 4, date: '01/05/2022',status:'finished'},
    {id: 3, name: 'Pawel Dennis', team: 6, date: '01/01/2023',status:'not started'},
    {id: 4, name: 'Zach Landry', team: 5, date: '23/04/2022',status:'finished'},
    {id: 5, name: 'Miriam Jefferson', team: 5, date: '05/05/2022',status:'finished'},
    {id: 6, name: 'Miriam Jefferson', team: 6, date: '12/08/2022',status:'not started'},
    {id: 7, name: 'Lukas Abbott', team: 2, date: '13/08/2022',status:'pending'},
    {id: 8, name: 'John Doe', team: 7, date: '13/08/2022',status:'pending'},
    {id: 9, name: 'Fluorine', team: 5, date: '30/09/2022',status:'pending'},
    {id: 10, name: 'Kiran Mcgowan', team: 3, date: '11/09/2022',status:'not started'},
    {id: 11, name: 'Tayla Woods', team: 3, date: '25/03/2022',status:'pending'},
    {id: 12, name: 'Vera Firth', team: 1, date: '13/08/2022',status:'pending'}
  ];
  dataSource = new MatTableDataSource<PeriodicElement>(this.ELEMENT_DATA);

  
  NOTES : Note[] = [
    {title : 'business lunch at Milan blablabla',start_time: new Date(2022,4,22,10,30),end_time: new Date(2022,4,22,11,30), color : 'lightgreen'},
    {title : 'Skype call with Kate',start_time: new Date(2022,4,22,13,0),end_time: new Date(2022,4,22,14,0), color : 'orange'},
    {title : 'HR team meeting',start_time: new Date(2022,4,22,16,0),end_time: new Date(2022,4,22,18,0), color : 'lightcoral'}

  ];
  loading = false;
  users: User[];

  
  currentUser: User;

  repartition: Repartition;
  items : Array<{name:string,count:number,color:string}>
  
  
 private _total:number =0;
  birthdays : any[];

 

  constructor(private loginUserService: LoginuserService, private userService : UserService) {
    
   }

  ngOnInit(): void {
    
    this.loading = true;
        this.loginUserService.getUsers().pipe(first()).subscribe(users => {
            this.loading = false;
            this.users = users;
        });

        this.userService.getBirthdays().subscribe( data => {
          this.birthdays=data;
        });

        this.userService.getGenderRepartition().subscribe(data => {
          this.repartition=data;
           this.items=[
            {name:'Male',count:this.repartition.male,color:'green'},
            {name:'Female',count:this.repartition.female,color:' #F0AB33 '}
            ];

            if(this.items.length>0)
       {
          this._total = this.items.map(a=>a.count).reduce((x,y)=>x+y);
        }
        }
        
        )
        
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  getPerimeter(radius:number):number
  {
    return Math.PI*2*radius;
  }

  getColor(index:number):string
  {
    return this.items[index].color;
  }

  getOffset(radius:number,index:number):number
  {   
    var percent=0;
    for(var i=0;i<index;i++)
    {
      percent+=((this.items[i].count)/this._total);
    }
    var perimeter = Math.PI*2*radius;
    return perimeter*percent;
  }

}
