import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http'
import { Product } from '../../models/product/product';
import { Category } from '../../models/category/category';
import { AbstractService } from '../abstract-service';
import { PageInfo } from 'src/app/models/pageInfo/pageInfo';

@Injectable({
  providedIn: 'root'
})
export class ProductService extends AbstractService<Product> {

  constructor(http: HttpClient) {
    super(http, 'product');
  }

  public getDataFromLinks(product: Product) {
    return this._http.get(product.links.find(x => x.rel == "category" ).href)
    .subscribe((cat: Category) => {
      product.category = cat;
      product.imagesUrl = new Array();
      product.links.filter(x => x.rel == "image" ).forEach(element => {
        product.imagesUrl.push(element.href);
      });
    });

  }

  public getPageFromCategory(page: number, categoryCode: string) {
    let params = new HttpParams().set("categoryCode", categoryCode);
    return this._http.get<Product[]>(this.hostAddress + this.endpointUrl + "/" + page, {params: params});
  }

  public getPageCountFromCategory(categoryCode: string) {
    let params = new HttpParams().set("categoryCode", categoryCode);
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + "/page/count",  {params: params});
  }
}
