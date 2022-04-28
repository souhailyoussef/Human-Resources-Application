import { DOCUMENT } from '@angular/common';
import { ValueConverter } from '@angular/compiler/src/render3/view/template';
import { Inject, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { Component, ElementRef, OnInit } from '@angular/core';
import { elections } from '@igniteui/material-icons-extended';
import { ChartType, ChartOptions } from 'chart.js';
import * as Highcharts from 'highcharts';
import { Rubrique } from 'src/app/rubrique';
import { NodeService } from 'src/app/services/node.service';

@Component({

  selector: 'app-widget-pie',
  templateUrl: './pie.component.html',
  styleUrls: ['./pie.component.css']
})



export class PieComponent implements OnInit {
    projets: Rubrique[];
    clients : Rubrique[];
    series : any[] = [];
    options : any;
    current_month = new Date().getMonth()+1;
    current_year  = new Date().getFullYear()
    date : number = this.current_month;
    dates = ['january','february','march','april','may','june','july','august','september','october','november','december',this.current_year.toString()]
    value_projects : any;

  highcharts = Highcharts;
 
  
  constructor(private nodeService : NodeService) {  
    this.loadContent(this.current_month);

    
   }

  ngOnInit(): void {
    this.nodeService.refreshNeeded$.subscribe(() => this.loadContent(this.current_month))
 
  }

  ngOnChanges() {
    //this.loadContent(this.date);

  }
  
  private loadContent(month : number) {
    this.nodeService.getData().subscribe(
      {
        
        // completeHandler
        error: () => {  /*this.handleError(this.error_message)*/},    // errorHandler 
        next: (data : any) => { 

            var production = data.children.filter((obj : Rubrique) => {
            return obj.name === 'production'
          })

          this.projets= production[0]?.children.filter((obj : Rubrique) => {
            return obj.name.toLowerCase() === 'projets'
          })
          this.clients=this.projets[0].children;
          this.value_projects=this.projets[0].value;
      
          for (let i=0;i< Object.keys(this.clients).length; i++) {
              var client = this.clients[i]
              this.series.push({
                    name : client.name,
                    y: this.calculatePercentage(this.value_projects,client.value),
                    value : client.value
              })
          }
          this.loadChart(this.series)
          
      }
      
      
  })



}

public calculatePercentage(total : number, value:number) : number{
    return (value/total)*100
}
public titleCaseWord(word: string) {
  if (!word) return word;
  return word[0].toUpperCase() + word.substr(1).toLowerCase();
}
loadChart(data : any) {
  this.options  =  {
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
        pointFormat: '{titleCaseWord(series.name) } <b>{point.value:,.0f} €</b>',
        style: {
          textTransform: "capitalize"
      }
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
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                  textTransform: "capitalize"
              }
            }
        }
    },
    series: [{
        name: 'Production',
        colorByPoint: true,
        data: data
    }]
}
} 


modo(event: any){
 
  let value : string = event.target.value.toLowerCase()
    switch (value) {
      case ('january') : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.january),
              value : client.january
        })
        this.loadChart(this.series)
    }

      break;
      case ('february') : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.february),
              value : client.february
        })
      }
        this.loadChart(this.series)

      break;
      case ('march') : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.march),
              value : client.march
        })
      }
        this.loadChart(this.series)
      break;
      case 'april' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.april),
              value : client.april
        })
      }
        this.loadChart(this.series)
      break;
      case 'may' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.may),
              value : client.may
        })
      }
        this.loadChart(this.series)
      break;
      case 'june' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.june),
              value : client.june
        })
      }
        this.loadChart(this.series)
      break;
      case 'july' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.july),
              value : client.july
        })
      }
        this.loadChart(this.series)
      break;
      case 'august' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.august),
              value : client.august
        })
      }
        this.loadChart(this.series)
      break;
      case 'september' :
        this.series = [];
        for (let i=0;i< Object.keys(this.clients).length; i++) {
          var client = this.clients[i]
          this.series.push({
                name : client.name,
                y: this.calculatePercentage(this.value_projects,client.september),
                value : client.september
          })
        }
          this.loadChart(this.series)
      break;
      case 'october' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.october),
              value : client.october
        })
      }
        this.loadChart(this.series)
      break;
      case 'november' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.november),
              value : client.november
        })
      }
        this.loadChart(this.series)
      break;
      case 'december' : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.december),
              value : client.december
        })
      }
        this.loadChart(this.series)
      break;
      default : 
      this.series = [];
      for (let i=0;i< Object.keys(this.clients).length; i++) {
        var client = this.clients[i]
        this.series.push({
              name : client.name,
              y: this.calculatePercentage(this.value_projects,client.value),
              value : client.value
        })
      }
        this.loadChart(this.series)
      break;

    
}
}




}

