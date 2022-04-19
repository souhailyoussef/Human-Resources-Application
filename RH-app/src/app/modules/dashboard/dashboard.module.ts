import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PieComponent } from './widgets/pie/pie.component';
import { AreaComponent } from './widgets/area/area.component';
import { NgChartsModule } from 'ng2-charts';
import { HighchartsChartModule } from 'highcharts-angular';
import { DashboardComponent } from './dashboard/dashboard.component';


@NgModule({
  declarations: [
    PieComponent,
    AreaComponent,
    DashboardComponent,
  ],
  imports: [
    CommonModule,
    NgChartsModule,
    HighchartsChartModule
  ]
})
export class DashboardModule { }
