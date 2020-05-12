import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { HttpClient } from '@angular/common/http';
import { Post } from '../../models/post/post';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService extends AbstractService<Post> {

  constructor(private http: HttpClient) { 
    super(http, "post");
  }

  getAllFromTopic(topicCode: string): Observable<Post[]>{
    return this._http.get<Post[]>(this.hostAddress + this.endpointUrl + "/byTopic/" + topicCode);
  }

  patchOwn(postCode: string, body: Post): Observable<Post>{
    return this._http.patch<Post>(this.hostAddress + this.endpointUrl + "/patchOwn/" + postCode, body);
  }

  deleteOwn(postCode: string): Observable<Post>{
    return this._http.delete<Post>(this.hostAddress + this.endpointUrl + "/deleteOwn/" + postCode);
  }


}