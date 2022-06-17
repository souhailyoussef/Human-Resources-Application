import { Component, Input, OnInit } from '@angular/core';
import { LoginuserService } from 'src/app/services/loginuser.service';
import jwt_decode from 'jwt-decode';
import { Router } from '@angular/router';
import { GlobalConstants } from 'src/app/global-constants';



@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {


  selection:any;
  role: string;
  //TODO : put in seperate file
menu_admin = GlobalConstants.menu_admin;
menu_collaborateur = GlobalConstants.menu_collaborator;
menu_manager = GlobalConstants.menu_manager;
menu_project_manager = GlobalConstants.menu_project_manager;
menu_accountant = GlobalConstants.menu_accountant





    constructor(private loginUserService : LoginuserService, public router : Router) { }


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
