import { Component, OnInit } from '@angular/core';
import { LoginuserService } from 'src/app/services/loginuser.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/user';
import jwt_decode from 'jwt-decode';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  currentUser : User;
  username: string;
  user : User;
  attributes : string[];
  values : any[];
  


  constructor(private userService : UserService, private loginUserService : LoginuserService, private route: ActivatedRoute) { 
    this.loginUserService.currentUser.subscribe(x => this.currentUser = x);

  }

  ngOnInit(): void {
    if (this.currentUser.access_token!=null) {
      var decoded_jwt = this.getDecodedAccessToken(this.currentUser.access_token);
      this.username = decoded_jwt.sub;

    }
    //TODO : clean code and give admin role to check other users profiles!!
    this.username = this.route.snapshot.paramMap.get('username') || '';
    this.loadUserDetails(this.username)
  



    
    
  }

  public loadUserDetails(username : string) {
    this.userService.getUserDetails(username).subscribe(data => {
      this.user = data;
      console.log(this.user);
      this.attributes = Object.keys(this.user);
      this.values = Object.values(this.user);
      console.log(this.values);
}

    )}
  
  getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    } catch(Error) {
      return null;
    }
  }

}
