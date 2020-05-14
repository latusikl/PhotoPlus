import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ProductService } from '../../services/product/product.service';
import { Product } from 'src/app/models/product/product';
import { CartService } from 'src/app/services/cart/cart.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'src/app/services/login/login.service';
import { BehaviorSubject } from 'rxjs';
import { Category } from 'src/app/models/category/category';
import { CategoryService } from 'src/app/services/category/category.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  @ViewChild("productName", { static: false })
  productNameTextArea: ElementRef;

  @ViewChild("productCategory", { static: false })
  productCategoryList: ElementRef;

  @ViewChild("productDescription", { static: false })
  productDescriptionTextArea: ElementRef;

  @ViewChild("productPrice", { static: false })
  productPriceTextArea: ElementRef;

  selectedCategoryCode: string;
  categories: Category[];
  param: string;
  product: BehaviorSubject<Product>;
  isEditing: boolean = false;

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private modalService: NgbModal,
    private loginService: LoginService,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    this.product = new BehaviorSubject<Product>({} as Product);
    this.route.paramMap.forEach(({ params }: Params) => {
      this.param = params.productCode;
      this.loadProduct();
    });
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    })
  }

  addToCart(product: Product) {
    this.cartService.addToCart(this.product.value);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = 'Please go to checkout to place an order.';
    modalRef.componentInstance.title = 'Added ' + product.name + ' to card.';
  }

  startEditing() {
    this.selectedCategoryCode = this.product.value.category.code;
    console.log(this.selectedCategoryCode);
    this.isEditing = true;
  }

  canEdit() {
    return this.loginService.isModerator;
  }

  patch() {
    console.log(this.productCategoryList.nativeElement.value);
    this.productService.patch(this.product.value.code, {
      description: this.productDescriptionTextArea.nativeElement.value,
      name: this.productNameTextArea.nativeElement.value,
      price: this.productPriceTextArea.nativeElement.value,
      categoryCode: this.productCategoryList.nativeElement.value
     } as Product).subscribe(res => {
      this.loadProduct();
      this.isEditing = false;
    });
  }

  loadProduct() {
    this.productService.getSingle(this.param).subscribe((data: Product) => {
      this.productService.getDataFromLinks(data);
      this.product.next(data);
    });
  }
}
