import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Product } from 'src/app/models/product/product';

interface Tuple{
  first: any,
  second: any,
}


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

  @Output("onDeletePhoto")
  onDeletePhoto = new EventEmitter<string>();

  constructor() { }

  ngOnInit(): void {
  }

  goBack(){
    this.onBack.emit()
  }

  addPhoto(){
    this.onAddPhoto.emit();
  }

  deletePhoto(code:string){
    this.onDeletePhoto.emit(code);
  }


  get urlAndCode():Tuple[]{
    const product = this.selectedProduct.value
    let array:Tuple[] = [];
    for(let [url, code] of [product.imagesUrl, product.imageCodes]){
      array.push({first: url, second: code});
    }
    return array;
  }
}
