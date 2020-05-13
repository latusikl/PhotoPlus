import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { CartService } from 'src/app/services/cart/cart.service';
import { Product } from '../../models/product/product';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  products: Product[];

  constructor( private productService: ProductService, private cartService: CartService, private modalService: NgbModal ) { }

  ngOnInit(): void {
              this.productService.getAllFromLink(environment.hostAddress + 'product/top').subscribe((data: Product[]) => {
                this.products = data;
                this.products.forEach(element => { this.productService.getDataFromLinks(element); });
              });
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = 'Please go to checkout to place an order.';
    modalRef.componentInstance.title = 'Added ' + product.name + ' to card.';
  }
}
