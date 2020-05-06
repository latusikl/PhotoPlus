import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Product } from 'src/app/models/product/product';

@Component({
  selector: 'app-image-carousel',
  templateUrl: './image-carousel.component.html',
  styleUrls: ['./image-carousel.component.scss']
})
export class ImageCarouselComponent implements OnInit {

  @Input("selectedProduct")
  selectedProduct: BehaviorSubject<Product>;

  @Output("onBack")
  onBack = new EventEmitter<any>();

  @Output("onAddPhoto")
  onAddPhoto = new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
  }

  goBack(){
    this.onBack.emit()
  }

  addPhoto(){
    this.onAddPhoto.emit();
  }
}
