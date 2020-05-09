import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Topic } from '../../models/topic/topic';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TopicService extends AbstractService<Topic> {

  constructor(http:HttpClient) { 
    super(http, "topic");
  }

  getAllFromSectionCode(sectionCode: string): Observable<Topic[]>{
    return this._http.get<Topic[]>(this.hostAddress + this.endpointUrl + "/bySection/" + sectionCode);
  }

  patchOwn(topicCode: string, body: Topic): Observable<Topic>{
    return this._http.patch<Topic>(this.hostAddress + this.endpointUrl + "/patchOwn/" + topicCode, body);
  }

  deleteOwn(topicCode: string): Observable<Topic>{
    return this._http.delete<Topic>(this.hostAddress + this.endpointUrl + "/deleteOwn/"+ topicCode);
  }

}
