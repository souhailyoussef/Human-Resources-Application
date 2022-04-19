import { Component, Input, OnInit } from '@angular/core';
import { LoginuserService } from 'src/app/services/loginuser.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {


  selection:any;
  role: string;
  //TODO : put in seperate file
  menu_admin : any[] = [['Home','home'],['Employees','person'],['Settings','settings']];
  menu_collaborateur : any[] = [['Home','home'],['Timesheets','calendar_today'],['Tasks','work'],['Projects','file_copy']];
  menu_chefDeProjet : any[] = [['Home','home'],['Timesheets','calendar_today'],['Projects','file_copy'],['Employees','person'],['Settings','settings'],['Dashboard','bubble_chart']];
  menu_manager : any[] = [['Home','home'],['Timesheets','calendar_today'],['Projects','file_copy'],['Employees','person'],['Dashboard','bubble_chart']];
  menu_comptable : any[] = [['Home','home'],['Projects','file_copy'],['Bills','attach_money']];

    constructor(private loginUserService : LoginuserService) { }


  ngOnInit(): void {
    this.selection="Home";
    this.role=this.getUserRole();

    
  }
 
  getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    } catch(Error) {
      return null;
    }
  }
  getUserRole() : string {
    const currentUser = this.loginUserService.currentUserValue;
    var decoded_jwt= this.getDecodedAccessToken(currentUser.access_token!)
    return decoded_jwt.roles[0]
  }

}
