import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs';
import { LoginuserService } from 'src/app/services/loginuser.service';
import { User } from 'src/app/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  loading = false;
  users: User[];
  currentUser: User;
  constructor(private loginUserService: LoginuserService) { }

  ngOnInit(): void {
    this.loading = true;
        this.loginUserService.getUsers().pipe(first()).subscribe(users => {
            this.loading = false;
            this.users = users;
        });
  }

}
