import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Product } from 'src/app/models/product/product';
import { Router } from '@angular/router';
import { Tuple } from "../../../../../helpers/tuple";




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

  @Output("onDeleteProduct")
  onDeleteProduct = new EventEmitter<string>();

  constructor(private router:Router) { }

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

  deleteProduct(code: string){
    this.onDeleteProduct.emit(code);
  }

  navigate(code: string){
    this.router.navigate(["imageDisplay", code]);
  }


  get urlAndCode():Tuple<string,string>[]{
    const product = this.selectedProduct.value
    let array:Tuple<string,string>[] = [];
    for(let [url, code] of [product.imagesUrl, product.imageCodes]){
      array.push({first: url, second: code});
    }
    return array;
  }
}
