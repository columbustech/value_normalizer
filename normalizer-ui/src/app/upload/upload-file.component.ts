import { Component, OnInit } from '@angular/core';
import { UploadFileService } from '../upload/upload-file.service';
import {HttpResponse, HttpEventType, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import { CookieService } from 'ng2-cookies';

@Component({
  selector: 'upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent implements OnInit {

  selectedFiles: FileList;
  currentFileUpload: File;
  url: string;
  token: string;
  code: string;
  progress: { percentage: number } = { percentage: 0 };

  constructor(private uploadService: UploadFileService,
              private router: Router, public cookieService: CookieService,
              private activeRoute: ActivatedRoute) { }

  ngOnInit() {
    // const routeParams = this.activeRoute.snapshot.params;
    // this.token = routeParams.token;
    this.code = this.activeRoute.snapshot.queryParamMap.get('code');
    if (this.code) {
      this.uploadService.getAuthToken(this.code).subscribe((response: any) => {
        if (response instanceof HttpResponse) {
          this.token = response.body.access_token;
          console.log(this.token);
          if (this.token) {
            this.cookieService.set('cdrive_token', this.token);
          } else {
            this.cookieService.delete('cdrive_token');
          }
        }
      }, error => {
        this.code = null;
        this.token = null; });
    }
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe((response: any) => {
      if (response.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * response.loaded / response.total);
      } else if (response instanceof HttpResponse) {
        const file = JSON.parse(response.body).file;
        console.log("Redirecting");
        this.router.navigate(['/file/header/', file]);
      } else if (response.type instanceof HttpErrorResponse) {
        console.log('Some Error occurred uploading');
      }
    }, );

    this.selectedFiles = undefined;
  }

  getFromCDrive() {
    this.progress.percentage = 0;
    console.log(this.token);
    this.uploadService.getFileFromCdrive(this.url, this.token).subscribe((response: any) => {
      if (response.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * response.loaded / response.total);
      } else if (response instanceof HttpResponse) {
        const file = JSON.parse(response.body).file;
        console.log("Redirecting");
        this.router.navigate(['/file/header/', file]);
      } else if (response.type instanceof HttpErrorResponse) {
        console.log('Some Error occurred uploading');
      }
    }, );

    this.url = undefined;
    this.token = undefined;
  }

  redirectToCDrive() {
    this.uploadService.getClientDetials().subscribe((response: any) => {
      if (response instanceof HttpResponse) {
        const client_id = response.body.client_id;
        const redirect_uri = response.body.redirect_uri;
        const auth_url = 'http://ad09282b27aca11e98ea412ac368fc7a-1539065101.us-east-1.elb.amazonaws.com/authentication/o/authorize/?response_type=code&client_id=' +
          client_id + '&redirect_uri=' + redirect_uri + '&state=1234xyz';
        window.location.href = auth_url;
      }
    });
  }

}
