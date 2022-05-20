import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'convertTime'
})
export class ConvertTimePipe implements PipeTransform {

  transform(value: number |undefined): string {
    var text = "0m";
    let hours,minutes;
    if (value == undefined) {
      return text
    }
    hours = Math.floor(value/60);
    minutes = Math.floor(value % 60);
    if (hours==0) {
      text=`${minutes}m`
    }
    else if (hours!=0 && minutes==0) {
      text=`${hours}h`
    }
    else if(hours!=0 && minutes !=0) {
      text=`${hours}h ${minutes}m`
    }
    return text
  }
  
}
