import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { LoginuserService } from 'src/app/services/loginuser.service';
import { User } from 'src/app/user';
import { MenuComponent } from '../menu/menu.component';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @Output() toggle: EventEmitter<any> = new EventEmitter();


  

  currentUser: User;
  username:string;
  activated:boolean=true;

    constructor(
        private router: Router,
        private loginUserService: LoginuserService
    ) {
        this.loginUserService.currentUser.subscribe(x => this.currentUser = x);
    }

    logout() {
        this.loginUserService.logout();
        this.router.navigate(['/login']);
    }

  ngOnInit(): void {
    if (this.currentUser.access_token!=null) {
      var decoded_jwt = this.getDecodedAccessToken(this.currentUser.access_token);
      this.username = decoded_jwt.sub;

    }
  }

  getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    } catch(Error) {
      return null;
    }
  }
  emit() {
    this.toggle.emit(null);
}

}
