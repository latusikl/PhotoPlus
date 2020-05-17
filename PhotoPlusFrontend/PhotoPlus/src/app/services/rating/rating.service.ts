
import { Injectable } from '@angular/core';
import { AbstractService } from '../abstract-service';
import { Rating } from 'src/app/models/rating/rating';
import { HttpClient } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class RatingService extends AbstractService<Rating>{

  constructor(http: HttpClient) {
    super(http, "rating");
  }
}
