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

  getAllFromSectionCode(sectionCode: number): Observable<Topic[]>{
    return this._http.get<Topic[]>(this.hostAddress + this.endpointUrl + "/bySection/" + sectionCode);
  }
}
