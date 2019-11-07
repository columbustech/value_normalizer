import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadFileService {

  constructor(private http: HttpClient) { }
  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    console.log('File' + file);
    const formdata: FormData = new FormData();

    formdata.append('file', file);

    const req = new HttpRequest('POST', 'api/value_normalizer/upload', formdata, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

  getFileFromCdrive(url: string, token: string): Observable<HttpEvent<{}>> {
    console.log('url' + url);

    const req = new HttpRequest('GET', 'api/value_normalizer/cdrive/download?url=' + url + '&token=' + token, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

  getClientDetials(): Observable<HttpEvent<{}>> {
    const req = new HttpRequest('GET', 'api/value_normalizer/client/details');
    return this.http.request(req);
  }

  getAuthToken(code): Observable<HttpEvent<{}>> {
    const req = new HttpRequest('GET', 'api/value_normalizer/client/token?code=' + code);
    return this.http.request(req);
  }
}
