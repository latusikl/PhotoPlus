import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category/category.service';
import { Category } from '../../models/category/category';
import { ProductService } from '../../services/product/product.service';
import { BehaviorSubject } from 'rxjs';
import { Product } from 'src/app/models/product/product';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  categories: BehaviorSubject<Category>[];

  products: BehaviorSubject<Product>[];

  currentCategoryCode: string;

  selectedPage: BehaviorSubject<number>;

  amountOfPages: BehaviorSubject<number>;

  isProductListEmpty = true;

  constructor( private categoryService: CategoryService,
               private productService: ProductService ) { }

  ngOnInit(): void {
    this.selectedPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    this.categories = new Array<BehaviorSubject<Category>>();
    this.categoryService.getAll().subscribe(async (data: Category[]) => {
      for (const category of data) {
        const categoryPageInfo = await this.productService.getPageCountFromCategory(category.code).toPromise();
        if (categoryPageInfo.pageAmount === 0) {
          continue;
        }
        this.categories.push(new BehaviorSubject(category));
      }
    });
  }

  async loadProductsFromCategory(categoryCode: string) {
    this.products = new Array<BehaviorSubject<Product>>();
    const pageInfo = this.productService.getPageCountFromCategory(categoryCode).toPromise();
    this.amountOfPages.next((await pageInfo).pageAmount);
    this.currentCategoryCode = categoryCode;
    this.productService.getPageOfAvailableProductsFromCategory(this.selectedPage.value, categoryCode).subscribe((data) => {
      this.isProductListEmpty = !(data.length > 0);
      for (const product of data) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }

  async changePage(page: number) {
    this.selectedPage.next(page);
    await this.loadProductsFromCategory(this.currentCategoryCode);
  }
}
