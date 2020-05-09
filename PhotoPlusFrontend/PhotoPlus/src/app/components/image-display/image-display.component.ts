import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ImageService } from 'src/app/services/image/image.service';

@Component({
  selector: 'app-image-display',
  templateUrl: './image-display.component.html',
  styleUrls: ['./image-display.component.scss']
})
export class ImageDisplayComponent implements OnInit {

  photoUri: string;

  constructor(private activatedRoute:ActivatedRoute, private imageService: ImageService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let photoCode = params.imageCode;
      
      this.photoUri = this.imageService.getLink(photoCode);
    })
  }

}
