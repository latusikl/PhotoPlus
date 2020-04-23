import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Section } from 'src/app/models/section/section';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SectionService extends AbstractService<Section> {

  constructor(httpClient: HttpClient) {
    super(httpClient, "section");
   }
}
