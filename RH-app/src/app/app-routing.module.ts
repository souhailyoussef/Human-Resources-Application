import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { TimesheetComponent } from './components/timesheet/timesheet.component';
import { TimesheetsComponent } from './components/timesheets/timesheets.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { DashboardParametersComponent } from './components/dashboard-parameters/dashboard-parameters.component';
import { AuthGuard } from './helpers/AuthGuard';
import { DashboardComponent } from './modules/dashboard/dashboard.component';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard],
  children: [{
    path:'',
    component:DashboardComponent
  }] },
  { path: 'login', component: UserLoginComponent },
  {path: 'timesheets', component:TimesheetsComponent},
  { path: 'timesheet/:id', component: TimesheetComponent },
  {
    path: 'profiles/profile/:username' , component:ProfileComponent,canActivate:[AuthGuard]
  },
  { path: 'settings', component: DashboardParametersComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
