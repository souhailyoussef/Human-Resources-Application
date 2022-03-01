import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { TimesheetComponent } from './components/timesheet/timesheet.component';
import { TimesheetsComponent } from './components/timesheets/timesheets.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { AuthGuard } from './helpers/AuthGuard';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: UserLoginComponent },
  {path: 'timesheets', component:TimesheetsComponent},
  { path: 'timesheet/:id', component: TimesheetComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
