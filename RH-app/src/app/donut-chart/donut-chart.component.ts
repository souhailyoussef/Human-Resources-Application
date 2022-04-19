import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';
//import {MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MatDatepicker} from '@angular/material/datepicker';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'; 

//import * as _moment from 'moment';
// tslint:disable-next-line:no-duplicate-imports
//import {default as _rollupMoment, _moment} from 'moment';



//const moment = _rollupMoment || _moment;

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const MY_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-donut-chart',
  templateUrl: './donut-chart.component.html',
  styleUrls: ['./donut-chart.component.css'],
  providers: [
    // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
    // application's root module. We provide it at the component level here, due to limitations of
    // our example generation script.
  ],
})

export class DonutChartComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  date = new FormControl();

  setMonthAndYear(normalizedMonthAndYear: any, datepicker: MatDatepicker<any>) {
    const ctrlValue = this.date.value;
    ctrlValue.month(normalizedMonthAndYear.month());
    ctrlValue.year(normalizedMonthAndYear.year());
    this.date.setValue(ctrlValue);
    datepicker.close();
  }
}
