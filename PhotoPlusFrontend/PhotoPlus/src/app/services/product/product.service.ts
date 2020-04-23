import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Product } from '../../models/product/product';
import { Category } from '../../models/category/category';
import { AbstractService } from '../abstract-service';

@Injectable({
  providedIn: 'root'
})
export class ProductService extends AbstractService<Product> {

  constructor(http: HttpClient) {
    super(http, 'product');
  }

  public getDataFromLinks(product: Product) {
    this._http.get(product.links.find(x => x.rel == "category" ).href)
    .subscribe((cat: Category) => {
      product.category = cat.name;
    });

    product.imagesUrl = new Array();
    product.links.filter(x => x.rel == "image" ).forEach(element => {
      product.imagesUrl.push(element.href);
    });

  }
}
