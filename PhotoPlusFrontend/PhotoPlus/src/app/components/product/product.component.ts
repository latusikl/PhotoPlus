import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ProductService } from '../../services/product/product.service';
import { Product } from 'src/app/models/product/product';
import { CartService } from 'src/app/services/cart/cart.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'src/app/services/login/login.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  @ViewChild("productName", { static: false })
  productNameTextArea: ElementRef;

  @ViewChild("productDescription", { static: false })
  productDescriptionTextArea: ElementRef;

  @ViewChild("productPrice", { static: false })
  productPriceTextArea: ElementRef;

  param: string;
  product: BehaviorSubject<Product>;
  isEditing: boolean = false;

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private modalService: NgbModal,
    private loginService: LoginService
  ) { }

  ngOnInit(): void {
    this.product = new BehaviorSubject<Product>({} as Product);
    this.route.paramMap.forEach(({ params }: Params) => {
      this.param = params.productCode;
    });
    this.loadProduct();
  }

  addToCart(product: Product) {
    this.cartService.addToCart(this.product.value);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = 'Please go to checkout to place an order.';
    modalRef.componentInstance.title = 'Added ' + product.name + ' to card.';
  }

  startEditing() {
    this.isEditing = true;
  }

  canEdit() {
    return this.loginService.isModerator;
  }

  patch() {
    const patchedProduct: Product = {
      description: this.productDescriptionTextArea.nativeElement.value,
      name: this.productNameTextArea.nativeElement.value,
      code: null,
      storeQuantity: null,
      category: null,
      price: null,
      imageCodes: null
    };
    this.isEditing = false;
    this.productService.patch(this.product.value.code, patchedProduct).subscribe(res => this.loadProduct());
  }

  loadProduct() {
    this.productService.getSingle(this.param).subscribe((data: Product) => {
      this.productService.getDataFromLinks(data);
      this.product.next(data);
    });
  }
}
