import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';
//import {MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MatDatepicker} from '@angular/material/datepicker';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'; 
import { Rubrique } from '../rubrique';
import { NodeService } from '../services/node.service';




@Component({
  selector: 'app-donut-chart',
  templateUrl: './donut-chart.component.html',
  styleUrls: ['./donut-chart.component.css'],

})

export class DonutChartComponent implements OnInit {

  production : Rubrique;
  charges : Rubrique;
  constructor(private nodeService : NodeService) { }

  ngOnInit(): void {
    this.nodeService.getData().subscribe( (data :any) => {

    
      var rubrique_production = data.children.filter((obj : Rubrique) => {
        return obj.name.toLowerCase() === 'production'
      })[0]
      this.production=rubrique_production
      var rubrique_charges = data.children.filter((obj : Rubrique) => {
        return obj.name.toLowerCase() === 'charges'
      })[0]
      this.charges = rubrique_charges;
    })

    
    
  }

 
}
