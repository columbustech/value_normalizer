import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LocalMergeService {

  constructor(private http: HttpClient) { }
  getFile(name: string) {
    return this.http.get('api/value_normalizer/file?name=' + name);
  }

  /*
  * Given name of the file, get its headers
  * */
  getHeaders(name: string) {
    return this.http.get('api/value_normalizer/file/header?name=' + name);
  }

  getColumnData(selectedColumn: number, name: string, sortOrder: number) {
    return this.http.get('api/value_normalizer/file/local?name=' + name + '&column='
      + selectedColumn + '&sort=' + sortOrder);
  }

  saveDiff(diff, selectedColumn: number, name: string) {
    // console.log('diff' + JSON.stringify(diff));
    const formdata: FormData = new FormData();

    formdata.append('keyval', JSON.stringify(diff));

    const req = new HttpRequest('POST', 'api/value_normalizer/local/diff/save?column=' + selectedColumn + '&name='
      + name, formdata, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }
}
