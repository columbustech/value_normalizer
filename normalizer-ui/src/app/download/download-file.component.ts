import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DownloadFileService} from './download-file.service';
import {HttpErrorResponse, HttpEventType, HttpResponse} from '@angular/common/http';
import {CookieService} from 'ng2-cookies';

@Component({
  selector: 'download-file',
  templateUrl: './download-file.component.html',
  styleUrls: ['./download-file.component.css']
})
export class DownloadFileComponent implements OnInit {
  fileName: string;
  token: string;
  message : string;
  constructor(private activeRoute: ActivatedRoute, private downloadFileService: DownloadFileService,
              private router: Router, private cookieService: CookieService) {
  }
  ngOnInit(): void {
    const routeParams = this.activeRoute.snapshot.params;
    this.fileName = routeParams.name;
    this.token = this.cookieService.get('cdrive_token');
  }

  download() {
    this.downloadFileService.downloadFile(this.fileName);
  }

  saveToCdrive() {
    this.message = '';
    this.downloadFileService.uploadFileToCdrive(this.fileName, this.token).subscribe( (response: any) => {
      if (response.type instanceof HttpErrorResponse) {
        console.log('Some Error Saving File to Cdrive');
        this.message = 'Unable to store file!';
      } else {
        this.message = 'Successfully Saved File!';
      }
    });
  }
}
