import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject, tap } from 'rxjs';
import { baseUrl } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor( private http: HttpClient) {
  }

  private _refreshNeeded$ = new Subject<void>()
  public loading: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false)

  public isLoading(state: boolean): void { this.loading.next(state) }


  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  getFiles() : Observable<any> {
    return this.http.get(`${baseUrl}/api/files`)
  }

  uploadFile(id : number, file : File, fileName ?: string) {
    const formData = new FormData();
    formData.append("file",file);
    return this.http.post(`${baseUrl}/api/upload/${id}`, formData).pipe(
      tap(() => {
        this._refreshNeeded$.next();
        this.loading.next(false)
      })
    );
  }

  downloadFile(id: number) : Observable<Blob>{
    return this.http.get(`${baseUrl}/api/files/${id}`,{
      responseType: 'blob'
    });
  }


  
 // uploadFile() : Observable<any> {

    //const body = new HttpParams().set('file', file)
     //const headers: new HttpHeaders().set('Content-Type', 'application/form-data')

    //return this.http.post(`${baseUrl}/api/upload`,file)
  //} 
}
