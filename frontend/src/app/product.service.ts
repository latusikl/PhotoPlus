import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Product } from './product';
import { Category } from './category';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productEndpoint = 'http://localhost:8090/product';

  constructor(private http: HttpClient) { }

  getProducts() {
    return this.http.get(this.productEndpoint + "/all/0");
  }

  addCategoryToProduct(product: Product) {
    this.http.get(product.links.find(x => x.rel == "category" ).href).subscribe((data: any) => {
      const cat: Category = data;
      product.category = cat.name;
    })
  }
}
