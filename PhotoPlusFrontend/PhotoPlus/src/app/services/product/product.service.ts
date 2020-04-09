import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Product } from '../../models/product/product';
import { Category } from '../../models/category/category';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productEndpoint = 'http://localhost:8090/product';

  constructor(private http: HttpClient) { }

  getProducts() {
    return this.http.get(this.productEndpoint + "/all/0");
  }

  getDataFromLinks(product: Product) {
    this.http.get(product.links.find(x => x.rel == "category" ).href).subscribe((data: any) => {
      const cat: Category = data;
      product.category = cat.name;
    });

    product.imagesUrl = new Array();
    product.links.filter(x => x.rel == "image" ).forEach(element => {
      product.imagesUrl.push(element.href);
      console.log(element.href);
    });

  }
}
