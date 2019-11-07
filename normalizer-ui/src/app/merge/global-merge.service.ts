import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GlobalMergeService {

  constructor(private http: HttpClient) {
  }

  getColumnData(name: string, selectedColumn: string) {
    return this.http.get('api/value_normalizer/file/global?name=' + name + '&column='
      + selectedColumn);
  }

  saveDiff(diff, selectedColumn: string, name: string) {
    // console.log('diff' + JSON.stringify(diff));
    const formdata: FormData = new FormData();
    formdata.append('keyval', JSON.stringify(diff));

    const req = new HttpRequest('POST', 'api/value_normalizer/global/diff/save?column=' + selectedColumn + '&name='
      + name, formdata, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }
}
