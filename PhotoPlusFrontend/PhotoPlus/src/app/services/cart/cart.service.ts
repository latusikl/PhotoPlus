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

  private items: BehaviorSubject<OrderItem>[];
  private price: BehaviorSubject<number>;

  constructor(private productService: ProductService, private modalService: NgbModal, private router: Router) {
    this.items = new Array<BehaviorSubject<OrderItem>>();
    const loadedItems: OrderItem[] = localStorage.getItem('items') ? JSON.parse(localStorage.getItem('items')) : [];
    loadedItems.forEach(it => {
      this.items.push(new BehaviorSubject(it));
    })
    this.price = new BehaviorSubject<number>(0);
    this.calculatePrice();
  }

  calculatePrice() {
    let sum = 0;
    this.items.forEach(element => {
      sum += (element.value.product.price * element.value.quantity);
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
    const index = this.items.findIndex(it => it.value.productCode == product.code);
    if (index > -1) {
      this.changeQuantity(this.items[index].value.quantity + 1, this.items[index].value)
    } else {
      this.items.push(new BehaviorSubject({ orderCode: null, productCode: product.code, product: product, quantity: 1 }));
      this.save();
    }
  }

  deleteFromCart(item: OrderItem) {
    const index = this.items.findIndex(it => it.value.productCode == item.productCode);
    if (index > -1) {
      this.items.splice(index, 1);
    }
    this.save();
  }

  getItems() {
    return this.items;
  }

  getItemsModel() {
    const array = new Array<OrderItem>();
    this.items.forEach(it => {
      array.push(it.value);
    })
    return array;
  }

  getSummaryPrice(): Observable<number> {
    return this.price.asObservable();
  }

  clearCart() {
    this.items = new Array<BehaviorSubject<OrderItem>>();
    this.save();
  }

  save() {
    const array = new Array<OrderItem>();
    this.items.forEach(item => {
      array.push(item.value)
    });
    localStorage.setItem('items', JSON.stringify(array));
    this.calculatePrice();
  }

  updateCartAndBuy() {
    this.items.forEach(it => {
      this.productService.getSingle(it.value.productCode).subscribe(product => {
        this.productService.getDataFromLinks(product);
        it.value.product = product;
        if (it.value.product.storeQuantity === 0) {
          const modalRef = this.modalService.open(ErrorModalComponent);
          modalRef.componentInstance.title = "Error occured!";
          modalRef.componentInstance.message = "Product is no longer available.";
          this.deleteFromCart(it.value);
          this.router.navigate(['/cart']);
        } else if (it.value.quantity > product.storeQuantity) {
          const modalRef = this.modalService.open(ErrorModalComponent);
          modalRef.componentInstance.title = "Error occured!";
          modalRef.componentInstance.message = "Product availability has changed.\n There are only " + product.storeQuantity +
            " " + product.name + " in store. Please change product quantity in cart.";
          this.changeQuantity(product.storeQuantity, it.value);
          this.router.navigate(['/cart']);
        }
      })
      this.router.navigate(['/order']);
    })
  }
}
