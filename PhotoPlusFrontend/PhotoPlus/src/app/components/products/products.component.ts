import { Component, OnInit } from '@angular/core';
import { ProductService } from "../../services/product/product.service";
import { Product } from '../../models/product/product';
import { CartService } from 'src/app/services/cart/cart.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject } from 'rxjs';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  products: BehaviorSubject<Product>[];

  selectedPage: BehaviorSubject<number>;
  amountOfPages: BehaviorSubject<number>;


  constructor(private productService: ProductService, private cartService: CartService, private modalService: NgbModal) { }

  async ngOnInit() {
    this.selectedPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    let pageInfo = this.productService.getPageCount().toPromise();
    this.loadProducts();
    let info = await pageInfo;
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  loadProducts(){
    this.products = new Array<BehaviorSubject<Product>>();
    this.productService.getPage(this.selectedPage.value).subscribe((data) => {
      for (let product of data) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }

  changePage(page:number){
    this.selectedPage.next(page);
    this.loadProducts();
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = "Please go to checkout to place an order.";
    modalRef.componentInstance.title = "Added " + product.name + " to card.";
  }

}
