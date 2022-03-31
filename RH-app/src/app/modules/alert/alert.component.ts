import { OnDestroy, ViewEncapsulation } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AlertSettings } from './alert-settings';
import { Alert } from './alert.model';
import { AlertService } from './alert.service';

@Component({
  selector: 'alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AlertComponent implements OnInit, OnDestroy {
  @Input() id = 'default-alert'
  @Input() fade = true

  alerts : Alert[] = [];
  alertSubscription : Subscription
  routeSubscription : Subscription
  

  constructor(private router: Router, private alertService: AlertService) { }
  ngOnDestroy(): void {
      this.alertSubscription.unsubscribe();
      this.routeSubscription.unsubscribe();
  }

  ngOnInit(): void {
    this.alertSubscription = this.alertService.onAlert(this.id)
    .subscribe(alert => {
      if (!alert.message) {
        this.alerts = [];
        return;
      }

      this.alerts.push(alert);

      if (alert.autoClose) {
        setTimeout(() => this.removeAlert(alert), 3000);
      }
    }
  );

  // clear alerts on location change
  this.routeSubscription = this.router.events.subscribe(event => {
    if (event instanceof NavigationStart) {
      this.alertService.clear(this.id);
    }
  });
  }

  removeAlert(alert: Alert) {
    if (!this.alerts.includes(alert)) return;

    if (this.fade) {
      this.alerts.find(x =>  x === alert)!.fade = true;

      setTimeout(() => {
          this.alerts = this.alerts.filter(x => x !== alert);
      }, 250);
    } else {
      this.alerts = this.alerts.filter(x => x !== alert);
    }
  }

  cssClass(alert: Alert) {
    if (!alert) return;

    const classes = ['alert', 'alert-dismissable'];
                
    const alertTypeClass = {
      [AlertSettings.SUCCESS]: 'alert alert-success',
      [AlertSettings.ERROR]: 'alert alert-danger',
      [AlertSettings.INFO]: 'alert alert-info',
      [AlertSettings.WARNING]: 'alert alert-warning'
    }

    classes.push(alertTypeClass[alert.alertType]);

    if (alert.fade) {
      classes.push('fade');
    }

    return classes.join(' ');
  }


}
