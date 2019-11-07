import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {GlobalMergeService} from './global-merge.service';
import {KeyList} from '../models/custom.interface';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';

@Component({
  selector: 'global-merge',
  templateUrl: './global-merge.component.html',
  styleUrls: ['./global-merge.component.css']
})
export class GlobalMergeComponent implements OnInit {
  fileName: string;
  column: string;
  columnData: KeyList[] = [];
  checked = {};
  itemsPerPage = 6;
  totalPages: number;
  index = 0;
  p = 1;
  display = [];
  globalDiff = [];
  constructor(private globalMergeService: GlobalMergeService,
              private activeRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    const routeParams = this.activeRoute.snapshot.params;
    this.fileName = routeParams.name;
    this.column = routeParams.column;
    this.globalMergeService.getColumnData(this.fileName, this.column).subscribe((response) => {
      for (let i = 0; i < JSON.parse(JSON.stringify(response)).length; i++) {
        this.columnData.push(JSON.parse(JSON.stringify(response))[i]);
      }
      console.log(this.columnData);
      this.totalPages = Math.ceil((this.columnData.length - 3) / this.itemsPerPage);
      this.display = this.columnData.slice(this.index + 3, this.columnData.length);
    });
    this.index = 0;
  }

  onCheckboxEvent(e, key, value) {
    console.log(key, " val: ", value);
    if (e.target.checked && !(key in this.checked)) {
      const values = [];
      values.push(value);
      this.checked[key] = values;
    } else if (e.target.checked && (key in this.checked)) {
      const values: string[] = this.checked[key];
      values.push(value);
      this.checked[key] = values;
    } else if (!e.target.checked && (key in this.checked)) {
      let values: string[] = this.checked[key];
      values = values.filter(val => val !== value);
      this.checked[key] = values;
     // this.checked = this.checked[key].filter(val => val !== value);
    }
  }

  merge() {
    this.findMax();
    this.saveDiff();
  }

  next() {
    this.index = this.index + 3;
    this.totalPages = Math.ceil(((this.columnData.length - 3 - this.index) / this.itemsPerPage) );
    this.display = this.columnData.slice(this.index + 3, this.columnData.length);
    this.p = 1;
  }

  findMax() {
    let max = '';
    for (const key in this.checked) {
      let mergeList: number[] = [];
      mergeList = mergeList.concat(this.columnData[key].values);
      if (max.length < this.columnData[key].key.length) {
        max = this.columnData[key].key;
      }
      const values: string[] = this.checked[key];
      for (let i = 0; i < values.length; i++) {
        const value = values[i];
        mergeList = mergeList.concat(this.columnData[value].values);
        if (max.length < this.columnData[value].key.length) {
          max = this.columnData[value].key;
        }
        this.reEditDom(key, value);
      }
      this.columnData[key].key = max;
      for (let i = 0; i < values.length; i++) {
        this.columnData[values[i]].key = max;
      }
      max = '';
      this.globalDiff.push(mergeList);
    }
  }

  reEditDom(key, value) {
    const check_element = <HTMLInputElement>document.getElementById(value + '-' + key);
    const label_element = <HTMLInputElement>document.getElementById('l-' + value);
    if (check_element) {
      check_element.checked = false;
    }
    if (label_element) {
      label_element.style.color = 'blue';
    }
  }

  saveDiff() {
    this.globalMergeService.saveDiff(this.globalDiff, this.column, this.fileName).subscribe((response: any) => {
      if (response.type instanceof HttpErrorResponse) {
        console.log('Some Error occurred uploading');
      } else {
        this.checked = {};
        this.globalDiff = [];
      }
    });
  }

  finish() {
    this.router.navigate(['/file/download', this.fileName]);
  }
}
