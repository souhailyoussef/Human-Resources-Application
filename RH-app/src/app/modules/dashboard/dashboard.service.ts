import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { baseUrl } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient) { }

  getGeneralInfo() : Observable<any> {
      return this.http.get<any>(`${baseUrl}/api/dashboard/info`);
    
  }
}
