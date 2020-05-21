import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Product } from '../../models/product/product';
import { Category } from '../../models/category/category';
import { AbstractService } from '../abstract-service';
import { PageInfo } from 'src/app/models/page-info/page-info';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService extends AbstractService<Product> {

  constructor(http: HttpClient) {
    super(http, 'product');
  }

  public getDataFromLinks(product: Product) {
    return this._http.get(product.links.find(x => x.rel === 'category').href)
      .subscribe((cat: Category) => {
        product.category = cat;
        product.imagesUrl = new Array();
        product.links.filter(x => x.rel === 'image').forEach(element => {
          product.imagesUrl.push(element.href);
        });
      });

  }

  public getPageFromCategory(page: number, categoryCode: string) {
    const params = new HttpParams().set('categoryCode', categoryCode);
    return this._http.get<Product[]>(this.hostAddress + this.endpointUrl + '/' + page, { params });
  }

  public getPageCountFromCategory(categoryCode: string) {
    const params = new HttpParams().set('categoryCode', categoryCode);
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + '/page/count', { params });
  }

  public mapToObj(strMap) {
    const obj = Object.create(null);
    for (const [k, v] of strMap) {
      obj[k] = v;
    }
    return obj;
  }

  public getSortedPage(page: number, sortedBy: string) {
    const params = new HttpParams().set('sortedBy', sortedBy);
    return this._http.get<Product[]>(this.hostAddress + this.endpointUrl + '/all/' + page, { params });
  }

  getProductsSearchByName(page: number, sortedBy: string, searchedText: string) {
    const params = new HttpParams().set('str', searchedText).set('sortedBy', sortedBy);
    return this._http.get<Product[]>(this.hostAddress + this.endpointUrl + '/search/' + page, { params });
  }

  getPageCountSearch(searchedText: string): Observable<PageInfo> {
    const params = new HttpParams().set('str', searchedText);
    return this._http.get<PageInfo>(this.hostAddress + this.endpointUrl + '/search/page/count', { params });
  }

  getAvgPurchasePrice(productCode: string): Observable<number> {
    const params = new HttpParams().set('code', productCode);
    return this._http.get<number>(this.hostAddress + this.endpointUrl + '/avgPrice', { params });
  }

}
