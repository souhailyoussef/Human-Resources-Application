import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { LoginuserService } from 'src/app/services/loginuser.service';
import { User } from 'src/app/user';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  user:User = new User();
  formGroup: FormGroup;
  loading = false;
  submitted = false;
  error = '';

  constructor(private loginUserService: LoginuserService,
    private route: ActivatedRoute,
    private router: Router
    ) {
      //redirect to home page if already logged in
      if (this.loginUserService.currentUserValue) { 
        //this.router.navigate(['/']);
    }
     }

  ngOnInit(): void {
    this.initForm();
  }
  initForm() {
    this.formGroup= new FormGroup({
      username: new FormControl("",[Validators.required]),
      password: new FormControl("",[Validators.required])
    })
  }

  userLogin() {
    this.user=this.formGroup.value;
    this.submitted = true;

        // stop here if form is invalid
        if (this.formGroup.invalid) {
            return;
        }

        this.loading = true;
        this.loginUserService.login(this.user.username,this.user.password)
        .pipe(first())
        .subscribe({
            next: () => {
                // get return url from route parameters or default to '/'
                const returnUrl = this.route.snapshot.queryParams['returnUrl']  || '/';
                this.router.navigate([returnUrl]);
            },
            error: error => {
                this.error = error;
                this.loading = false;
            }
        });
}
}
