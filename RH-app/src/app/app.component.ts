import { Component, Input, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { LoginuserService } from './services/loginuser.service';
import { User } from './user';
import {BreakpointObserver} from '@angular/cdk/layout';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'RH-app';

  currentUser: User;
  firstSelected:boolean;
  public leftOpened = true;
  

    constructor(
        public router: Router,
        private loginUserService: LoginuserService,
        private observer : BreakpointObserver
    ) 
    {
        this.loginUserService.currentUser.subscribe(x => this.currentUser = x);
    }

  
    toggle() {
      this.leftOpened = !this.leftOpened;
    }

    
}
