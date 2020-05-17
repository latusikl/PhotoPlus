import { Injectable } from '@angular/core';
import { Product } from 'src/app/models/product/product';
import { BehaviorSubject, Observable } from 'rxjs';
import { OrderItem } from 'src/app/models/orderItem/order-item';
import { ProductService } from '../product/product.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorModalComponent } from 'src/app/components/error-modal/error-modal.component';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  items: OrderItem[];
  private price: BehaviorSubject<number>;

  constructor(private productService: ProductService, private modalService: NgbModal, private router: Router) {
    this.items = localStorage.getItem('items') ? JSON.parse(localStorage.getItem('items')) : [];
    this.price = new BehaviorSubject<number>(0);
    this.calculatePrice();
  }

  calculatePrice() {
    let sum = 0;
    this.items.forEach(element => {
      sum += (element.product.price * element.quantity);
    });
    this.price.next(+sum.toFixed(2));
  }

  changeQuantity(value: number, item: OrderItem) {
    if (value <= item.product.storeQuantity) {
      item.quantity = value;
    } else if (value <= 0) {
      item.quantity = 1;
    }
    this.save();
  }

  addToCart(product: Product) {
    const index = this.items.findIndex(it => it.productCode == product.code);
    if (index > -1) {
      this.changeQuantity(this.items[index].quantity + 1, this.items[index])
    } else {
      this.items.push({ orderCode: null, productCode: product.code, product: product, quantity: 1 });
      this.save();
    }
  }

  deleteFromCart(item: OrderItem) {
    const index = this.items.findIndex(it => it.productCode == item.productCode);
    if (index > -1) {
      this.items.splice(index, 1);
    }
    this.save();
  }

  getItems() {
    return this.items;
  }

  getSummaryPrice(): Observable<number> {
    return this.price.asObservable();
  }

  clearCart() {
    this.items = new Array<OrderItem>();
    this.save();
  }

  save() {
    localStorage.setItem('items', JSON.stringify(this.items));
    this.calculatePrice();
  }

  updateCartAndBuy() {
    this.items.forEach(it => {
      this.productService.getSingle(it.productCode).subscribe(product => {
        it.product = product;
        if (it.product.storeQuantity == 0) {
          const modalRef = this.modalService.open(ErrorModalComponent);
          modalRef.componentInstance.title = "Error occured!";
          modalRef.componentInstance.message = "Product is no longer available.";
          this.deleteFromCart(it);
          this.router.navigate(['/cart']);
        } else if (it.quantity > product.storeQuantity) {
          const modalRef = this.modalService.open(ErrorModalComponent);
          modalRef.componentInstance.title = "Error occured!";
          modalRef.componentInstance.message = "Product availability has changed.\n There are only " + product.storeQuantity +
            " " + product.name + " in store. Please change product quantity in cart.";
          this.changeQuantity(product.storeQuantity, it);
          this.router.navigate(['/cart']);
        }
      })
      this.router.navigate(['/order']);
    })
  }
}
