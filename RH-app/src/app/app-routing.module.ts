import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { TimesheetsComponent } from './components/timesheets/timesheets.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { DashboardParametersComponent } from './components/dashboard-parameters/dashboard-parameters.component';
import { AuthGuard } from './helpers/AuthGuard';
import { DashboardComponent } from './modules/dashboard/dashboard/dashboard.component';
import { TasksComponent } from './components/tasks/tasks.component';
import { WeeklyTimesheetComponent } from './components/weekly-timesheet/weekly-timesheet.component';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'login', component: UserLoginComponent },
  {path: 'timesheets', component:TimesheetsComponent, children: [{
    path:'weekly-timesheet', component : WeeklyTimesheetComponent
  }]},
  {
    path: 'profiles/profile/:username' , component:ProfileComponent,canActivate:[AuthGuard]
  },
  { path: 'settings', component: DashboardParametersComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'tasks', component : TasksComponent},

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
