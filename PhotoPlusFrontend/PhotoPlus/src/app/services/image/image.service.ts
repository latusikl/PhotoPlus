import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ImageResponse } from "../../models/image/imageResponse";


@Injectable({
  providedIn: 'root'
})
export class ImageService {

  protected hostAddress = environment.hostAddress;

  constructor(private httpClient: HttpClient) { }

  post(file: File){
    let formData = new FormData();
    formData.append('file',file,file.name);
    return this.httpClient.request<ImageResponse>('POST',this.hostAddress + "image",{body: formData});
  };

  delete(code: string){
    return this.httpClient.delete(this.hostAddress + "image/delete/" + code);
  }
}
