import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class ImageService {

  protected hostAddress = environment.hostAddress;
  protected endpointUrl = "image";

  constructor(private httpClient: HttpClient) { }

  getLink(code: string){
    return `${this.hostAddress}${this.endpointUrl}/${code}`;
  }

  post(file: File){
    let formData = new FormData();
    formData.append('file',file,file.name);
    return this.httpClient.request<HttpResponse<any>>('POST',this.hostAddress + "image",{body: formData, observe: 'response'});
  };

  delete(code: string){
    return this.httpClient.delete(this.hostAddress + "image/delete/" + code);
  }
}
