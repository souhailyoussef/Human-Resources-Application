import { DOCUMENT } from '@angular/common';
import { ElementRef, Inject, ViewChild } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import { Rubrique } from 'src/app/rubrique';
import { NodeService } from 'src/app/services/node.service';


@Component({
  selector: 'app-widget-area',
  templateUrl: './area.component.html',
  styleUrls: ['./area.component.css']
})
export class AreaComponent implements OnInit {

 
  highcharts = Highcharts;
  series_charges : any = [];
  series_productions : any = [];
  series_ETBDA :any= [];

  options : any;
  series: any[]
     
  
  constructor(private nodeService : NodeService) { }


  ngOnInit(): void {
    this.loadContent()
  }
    loadContent() {
    this.nodeService.getData().subscribe(
      {
        
        // completeHandler
        error: () => {  /*this.handleError(this.error_message)*/},    // errorHandler 
        next: (data : any) => {         
            var production = data.children.filter((obj : Rubrique) => {
                return obj.name.toLowerCase() === 'production'
              })[0]
               var charges  = data.children.filter((obj : Rubrique) => {
                return obj.name.toLowerCase() === 'charges'
              })[0]
        
              this.series_charges = [charges.january,charges.february,charges.march,charges.april,charges.may,charges.june,charges.july,charges.august,charges.september,charges.october,charges.november,charges.december]
              this.series_productions = [production.january,production.february,production.march,production.april,production.may,production.june,production.july,production.august,production.september,production.october,production.november,production.december]
              for (let i=0; i<12; i++) {
                  this.series_ETBDA.push( Math.trunc((this.series_productions[i] - this.series_charges[i])*0.9))
              }


              this.options   = {

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
                tooltip : {
                    
                style : {
                    textTransform: "capitalize",

                }
            },
                series: [{
                    name: 'Charges',
                    data: this.series_charges
                }, {
                    name: 'Production',
                    data: this.series_productions
            
                }, {
                    name: 'EBTDA',
                    data: this.series_ETBDA
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
         }
        })

      }


 
  }
  

 

  
  



