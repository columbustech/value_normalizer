import {Component, Input, OnInit} from '@angular/core';
import {LocalMergeService} from './local-merge.service';
import { KeyValue } from '../models/custom.interface';
import {Router} from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
@Component({
  selector: 'column-data',
  templateUrl: './column-data.component.html',
  styleUrls: ['./column-data.component.css']
})
export class ColumnDataComponent implements OnInit {

  @Input() selectedColumn;
  @Input() fileName;
  columnData: KeyValue[] = [];
  checked: KeyValue[] = [];
  toMergeData = [];
  p = 1;
  itemsPerPage = 30;
  totalPages: number;
  constructor(private localMergeService: LocalMergeService,
              private router: Router) {}

  ngOnInit() {
     this.localMergeService.getColumnData(this.selectedColumn, this.fileName, 1)
       .subscribe( response => {
      for (let i = 0; i < JSON.parse(JSON.stringify(response)).length; i++) {
        this.columnData.push(JSON.parse(JSON.stringify(response))[i]);
      }
      this.totalPages = Math.ceil(this.columnData.length / this.itemsPerPage);
    });
  }

  onCheckboxEvent(e, item) {
    if (e.target.checked && !this.checked.includes(item)) {
      this.checked.push(item);
    } else if (!e.target.checked && this.checked.includes(item)) {
      this.checked = this.checked.filter(val => val !== item);
    }

  }

  merge() {
    this.toMergeData.push(this.checked);
    this.reEditDom();
    this.checked = [];
    console.log(this.toMergeData);
    this.localMergeService.saveDiff(this.toMergeData, this.selectedColumn, this.fileName).subscribe((response: any) => {
      if (response.type instanceof HttpErrorResponse) {
        console.log('Some Error occurred uploading');
      } else {
        this.toMergeData = [];
      }
    });
  }

  reEditDom() {
    let maxValue = '';
    for (let i = 0; i < this.checked.length; i++) {
      if (this.checked[i].value.length > maxValue.length) {
        maxValue = this.checked[i].value;
      }
    }
    for (let i = 0; i < this.checked.length; i++) {
      let element = <HTMLInputElement>document.getElementById(this.checked[i].index.toString());
      if (element) {
        element.checked = false;
        element.labels.forEach(label => {
          label.innerHTML = maxValue;
          label.style.color = 'blue';
        });
      }
      // element.innerHTML = maxValue;
      // console.log(element);
    }
  }
  save() {
    this.localMergeService.saveDiff(this.toMergeData, this.selectedColumn, this.fileName).subscribe((response: any) => {
      if (response instanceof HttpResponse) {
        this.router.navigate(['/file/global', this.fileName, this.selectedColumn]);
      } else if (response.type instanceof HttpErrorResponse) {
        console.log('Some Error occurred uploading');
      }
    });
  }
}
