import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HomeComponent } from './components/home/home.component';
import { ErrorInterceptor } from './helpers/ErrorInterceptor';
import { JwtInterceptor } from './helpers/JwtInterceptor';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './components/navbar/navbar.component';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MenuComponent } from './components/menu/menu.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { TimesheetsComponent } from './components/timesheets/timesheets.component';
import { TimesheetComponent } from './components/timesheet/timesheet.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTreeModule } from '@angular/material/tree';
import { ProfileComponent } from './components/profile/profile.component';
import { DashboardParametersComponent } from './components/dashboard-parameters/dashboard-parameters.component';
import {MatTableModule} from '@angular/material/table'; 
import {AngularTreeGridModule} from 'angular-tree-grid';
import { TableRowComponent } from './components/table-row/table-row.component';

import {TreeTableModule} from 'primeng/treetable';
import {Button, ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {MultiSelectModule} from 'primeng/multiselect';
import {InputTextModule} from 'primeng/inputtext';
import {ToastModule} from 'primeng/toast';
import {ContextMenuModule} from 'primeng/contextmenu';
import { DialogBoxComponent } from './components/dialog-box/dialog-box.component';
import {MatDialogModule} from '@angular/material/dialog'; 
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { SearchByRubriqueNamePipe } from './pipes/search-by-rubrique-name.pipe'; 
import {MatTooltipModule} from '@angular/material/tooltip'; 
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { AlertComponent } from './modules/alert/alert.component';
import {MatDividerModule} from '@angular/material/divider'; 
import { FlexLayoutModule } from '@angular/flex-layout';
import { PieComponent } from './modules/dashboard/widgets/pie/pie.component';
import { AreaComponent } from './modules/dashboard/widgets/area/area.component';
import { NgChartsModule } from 'ng2-charts';
import { DonutChartComponent } from './donut-chart/donut-chart.component';
import { HighchartsChartModule } from 'highcharts-angular';
import { DashboardComponent } from './modules/dashboard/dashboard/dashboard.component';
import {MatGridListModule} from '@angular/material/grid-list'; 
import { MatPaginatorModule } from '@angular/material/paginator';





@NgModule({
  declarations: [
    AppComponent,
    UserLoginComponent,
    HomeComponent,
    NavbarComponent,
    MenuComponent,
    TimesheetsComponent,
    TimesheetComponent,
    ProfileComponent,
    DashboardParametersComponent,
    TableRowComponent,
    DialogBoxComponent,
    SearchByRubriqueNamePipe,
    AlertComponent,
    PieComponent,
    AreaComponent,
    DonutChartComponent,
    DashboardComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    MatMenuModule,
    MatIconModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatButtonModule,
    BrowserAnimationsModule,
    CdkAccordionModule,
    CdkTreeModule,
    MatSidenavModule,
    MatDividerModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTreeModule,
    MatTableModule,
    AngularTreeGridModule,
    TreeTableModule,
    DialogModule,
    ButtonModule,
    MatDialogModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    FlexLayoutModule,
    NgChartsModule,
    HighchartsChartModule,
    MatGridListModule,
    MatPaginatorModule

  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
