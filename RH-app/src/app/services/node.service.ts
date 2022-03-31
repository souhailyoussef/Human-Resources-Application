import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, tap } from 'rxjs';
import { baseUrl } from 'src/environments/environment';
import { Rubrique } from '../rubrique';
@Injectable({
  providedIn: 'root'
})
export class NodeService {

  constructor(private http: HttpClient) { }
  private _refreshNeeded$ = new Subject<void>();

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  getData() : Observable<Rubrique[]> {
    return this.http.get<Rubrique[]>(`${baseUrl}/api/nodes`);
  }

  saveNode(parent_id:number, name:string, value?: number) : Observable<Rubrique> {
    return this.http.post<Rubrique>(`${baseUrl}/api/node/save`,{'parent_id' : parent_id, 'name': name, 'value' : value})
    .pipe(
      tap(() => {
        this._refreshNeeded$.next();
      }) 
    );

    
  }

  deleteNode(id: number) : Observable<any> {
    return this.http.delete(`${baseUrl}/api/node/${id}`)
    .pipe(
      tap(() => {
        this.refreshNeeded$.next();
      }
    ))
  }
  
  changeNodeName(id:number,newName: string) : Observable<any> {
    return this.http.patch(`${baseUrl}/api/node/${id}`,newName)
    .pipe(
      tap(() => {
        this._refreshNeeded$.next();
      })
    )
  }

 

  
  
}
interface NodeForm {
  parent_name: string;
  name: string;
  value : number;
}

