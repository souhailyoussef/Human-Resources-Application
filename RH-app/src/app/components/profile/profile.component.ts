import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { LoginuserService } from 'src/app/services/loginuser.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/user';
import jwt_decode from 'jwt-decode';
import { ActivatedRoute } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';



export interface UserData {
  task_name: string;
  project_name: string;
  project_status: string;
  task_end_date: string;
}

/** Constants used to fill up our data base. */
const FRUITS: string[] = [
  'blueberry',
  'lychee',
  'kiwi',
  'mango',
  'peach',
  'lime',
  'pomegranate',
  'pineapple',
];
const NAMES: string[] = [
  'Maia',
  'Asher',
  'Olivia',
  'Atticus',
  'Amelia',
  'Jack',
  'Charlotte',
  'Theodore',
  'Isla',
  'Oliver',
  'Isabella',
  'Jasper',
  'Cora',
  'Levi',
  'Violet',
  'Arthur',
  'Mia',
  'Thomas',
  'Elizabeth',
];

/**
 * @title Data table with sorting, pagination, and filtering.
 */


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit,AfterViewInit {

  currentUser : User;
  username: string;
  user : User;
  attributes : string[];
  values : any[];
  status : boolean;
  tasks : Array<any>;
  current_date : Date = new Date()
  

  displayedColumns: string[] = ['task_name', 'project_name', 'project_status', 'task_end_date'];
  dataSource: MatTableDataSource<UserData>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(private userService : UserService, private loginUserService : LoginuserService, private route: ActivatedRoute) { 
    this.loginUserService.currentUser.subscribe(x => this.currentUser = x);


     // Create 100 users
     

    

  }

  ngOnInit(): void {
    if (this.currentUser.access_token!=null) {
      var decoded_jwt = this.getDecodedAccessToken(this.currentUser.access_token);
      this.username = decoded_jwt.sub;

    }
    //TODO : clean code and give admin role to check other users profiles!!
    this.username = this.route.snapshot.paramMap.get('username') || '';
    this.loadUserDetails(this.username)

     console.log("datasource  : ",this.dataSource.data)
     this.status=true;

  
    
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }





  public loadUserDetails(username : string) {
    this.userService.getUserDetails(username).subscribe(data => {
      this.user = data;
      this.attributes = Object.keys(this.user);
      this.values = Object.values(this.user);
        //load current tasks and projects
    this.userService.getCurrentTasksAndProjects(this.user.id, this.convertDateToText(this.current_date)).subscribe(
      res => {
        this.tasks=res;

             // Assign the data to the data source for the table to render
                         
     this.dataSource = new MatTableDataSource(this.tasks);
      }
    )
     
}
    )
  }
  
  getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    } catch(Error) {
      return null;
    }
  }

  getValueByAttribute(attribute: string ) : any {
  }

  toggleEdit() {
      this.status= !this.status;
  }
  convertDateToText(date : Date) : string {

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
