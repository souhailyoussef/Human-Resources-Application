import { DOCUMENT } from '@angular/common';
import { Inject, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Component, ElementRef, OnInit } from '@angular/core';
import { elections } from '@igniteui/material-icons-extended';
import { ChartType, ChartOptions } from 'chart.js';
import * as Highcharts from 'highcharts';


@Component({

  selector: 'app-widget-pie',
  templateUrl: './pie.component.html',
  styleUrls: ['./pie.component.css']
})



export class PieComponent implements OnInit {

  highcharts = Highcharts;
  options : any =  {
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
    },
    title: {
        text: 'Production par client'
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    accessibility: {
        point: {
            valueSuffix: '%'
        }
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
            }
        }
    },
    series: [{
        name: 'Production',
        colorByPoint: true,
        data: [{
            name: 'Client 1',
            y: 61.41,
            sliced: true,
            selected: true
        }, {
            name: 'Client 2',
            y: 11.84
        }, {
            name: 'Client 3',
            y: 10.85
        }, {
            name: 'Client 4',
            y: 4.67
        }, {
            name: 'Client 5',
            y: 4.18
        }, {
            name: 'Client 6',
            y: 1.64
        }, {
            name: 'Client 7',
            y: 1.6
        }, {
            name: 'Client 8',
            y: 1.2
        }, {
            name: 'Client 9',
            y: 2.61
        }]
    }]
}
  
  constructor() {   }

  ngOnInit(): void {
  }

}


