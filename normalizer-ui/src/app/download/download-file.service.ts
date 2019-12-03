import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DownloadFileService {
  constructor(private http: HttpClient) { }

  downloadFile(fileName: string) {
    window.location.href = 'api/value_normalizer/file/download/' + fileName;
  }

  uploadFileToCdrive(uploadurl: string, url: string, token: string,): Observable<HttpEvent<{}>> {
    console.log('url' + url);
    const req = new HttpRequest('POST', 'api/value_normalizer/cdrive/upload?name=' + url + '&token=' + token + '&uploadurl=' + uploadurl, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }
}
