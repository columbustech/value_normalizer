import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LocalMergeService} from './local-merge.service';

@Component({
  selector: 'local-merge',
  templateUrl: './local-merge.component.html',
  styleUrls: ['./local-merge.component.css']
})
export class LocalMergeComponent implements OnInit {
  showCloumnData = false;
  columnHeaders: {};
  fileName: string;
  selectedColumn = 0;
  // test: string;
  constructor(private activeRoute: ActivatedRoute,
              private localMergeService: LocalMergeService,
              private router: Router) {
  }
  ngOnInit() {
    const routeParams = this.activeRoute.snapshot.params;
    this.fileName = routeParams.name;
    this.localMergeService.getHeaders(this.fileName).subscribe((response) => {
      this.columnHeaders = response;
    });
  }
  getColumnKey(key) {
    this.showCloumnData = true;
  }
}
