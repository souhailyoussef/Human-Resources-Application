import { DOCUMENT } from '@angular/common';
import { ElementRef, Inject, ViewChild } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';


@Component({
  selector: 'app-widget-area',
  templateUrl: './area.component.html',
  styleUrls: ['./area.component.css']
})
export class AreaComponent implements OnInit {

 
  highcharts = Highcharts;
  options : any = {

    title: {
        text: 'Evolution Production/Charges/EBTDA'
    },

    subtitle: {
        text: 'Année : 2021'
    },

    yAxis: {
        title: {
            text: 'Production'
        }
    },

    xAxis: {
        accessibility: {
            rangeDescription: 'Range: 1 to 12'
        },
        categories: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']

    },

    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle'
    },

    plotOptions: {
        series: {
            label: {
                connectorAllowed: false
            },
            pointStart: 0,
        }
    },

    series: [{
        name: 'Charges',
        data: [21850,21300,20850,24400,22218,23619,25780,23960,23960,24100,23962,23962]
    }, {
        name: 'Production',
        data: [23200,22000,22000,24400,24400,24400,24000,24400,24400,24400,24400,24400]

    }, {
        name: 'EBTDA',
        data: [1348,680,110,307,2182,438,-1380,438,438,221,438,438]
    }],

    responsive: {
        rules: [{
            condition: {
                maxWidth: 500
            },
            chartOptions: {
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                }
            }
        }]
    }
  }
  
 

    
    charges = [21850,21300,20850,24400,22218,23619,25780,23960,23960,24100,23962,23962]

    production = [23200,22000,22000,24400,24400,24400,24000,24400,24400,24400,24400,24400]

    ebtda= [1348,680,110,307,2182,438,-1380,438,438,221,438,438]

    
  
  constructor() { }


  ngOnInit(): void {
    
  }

 

 
  }
  

 

  
  



