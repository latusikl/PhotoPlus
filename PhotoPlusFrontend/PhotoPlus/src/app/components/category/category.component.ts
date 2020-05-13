import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../services/category/category.service";
import { Category } from '../../models/category/category';
import { ProductService } from "../../services/product/product.service";
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from '../../models/product/product';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject } from 'rxjs';

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

  constructor(private categoryService: CategoryService, private productService: ProductService, private cartService: CartService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.selectedPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    this.categories = new Array<BehaviorSubject<Category>>();
    this.categoryService.getAll().subscribe((data: Category[]) => {
      for (let category of data) {
        this.categories.push(new BehaviorSubject(category));
      }
    });
  }

  async loadProductsFromCategory(categoryCode: string) {
    this.products = new Array<BehaviorSubject<Product>>();
    let pageInfo = this.productService.getPageCountFromCategory(categoryCode).toPromise();
    let info = await pageInfo;
    this.amountOfPages.next((await pageInfo).pageAmount);
    this.currentCategoryCode = categoryCode;
    this.productService.getPageFromCategory(this.selectedPage.value, categoryCode).subscribe((data) => {
      for (let product of data) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = "Please go to checkout to place an order.";
    modalRef.componentInstance.title = "Added " + product.name + " to card.";
  }

  changePage(page:number){
    this.selectedPage.next(page);
    this.loadProductsFromCategory(this.currentCategoryCode);
  }
}
