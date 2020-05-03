import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../services/category/category.service";
import { Category } from '../../models/category/category';
import { ProductService } from "../../services/product/product.service";
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from '../../models/product/product';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {

  categories: Category[];
  products: Product[];
  productsToShow: Product[];

  constructor( private categoryService: CategoryService, private productService: ProductService, private cartService: CartService, private modalService: NgbModal ) { }

  ngOnInit(): void {
      this.categoryService.getAll().subscribe((data: Category[]) => {
        this.categories = data;
      });
          this.productService.getAll().subscribe((data: Product[]) => {
            this.products = data;
            this.products.forEach(element => { this.productService.getDataFromLinks(element) });
          });
  }

  filterProducts(categoryName: String) {
      this.productsToShow = [];
      this.products.forEach(product => {
        console.log(categoryName, product);
        if (categoryName == product.category) {
          this.productsToShow.push(product);
        }
      });
    }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = "Please go to checkout to place an order.";
    modalRef.componentInstance.title = "Added " + product.name + " to card.";
  }
}
