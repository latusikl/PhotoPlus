import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ProductService } from "../../services/product/product.service";
import { Product } from 'src/app/models/product/product';
import { CartService } from 'src/app/services/cart/cart.service';
import { SuccessModalComponent } from '../success-modal/success-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorModalComponent } from '../error-modal/error-modal.component';
import { LoginService } from 'src/app/services/login/login.service';
import { BehaviorSubject } from 'rxjs';
import { Category } from 'src/app/models/category/category';
import { CategoryService } from 'src/app/services/category/category.service';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  @ViewChild('productName', { static: false })
  productNameTextArea: ElementRef;

  @ViewChild('productCategory', { static: false })
  productCategoryList: ElementRef;

  @ViewChild('productDescription', { static: false })
  productDescriptionTextArea: ElementRef;

  @ViewChild('productPrice', { static: false })
  productPriceTextArea: ElementRef;

  selectedCategoryCode: string;
  categories: Category[];
  param: string;
  product: BehaviorSubject<Product>;
  isEditing = false;
  linksForm: FormGroup;
  submitted = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private productService: ProductService,
              private cartService: CartService,
              private modalService: NgbModal,
              private loginService: LoginService,
              private categoryService: CategoryService,
              private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.product = new BehaviorSubject<Product>({} as Product);
    this.route.paramMap.forEach(({ params }: Params) => {
      this.param = params.productCode;
      this.loadProduct();
    });
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    });
    this.linksForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      link: ['', [Validators.required, Validators.pattern(/((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/)]],
    });
  }

  buy(product: Product) {
    if (!confirm("Are you sure you want to buy " + product.name + "? \n This operation will clear your shopping cart.")) {
      return;
    }
    this.cartService.clearCart();
    this.cartService.addToCart(product);
    if (this.loginService.isLoggedIn() == false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = "Error occured!";
      modalRef.componentInstance.message = "Please login!.";
      return;
    }
    this.router.navigate(['/order']);
  }

  addToCart(product: Product) {
    this.cartService.addToCart(this.product.value);
    const modalRef = this.modalService.open(SuccessModalComponent);
    modalRef.componentInstance.message = 'Please go to checkout to place an order.';
    modalRef.componentInstance.title = 'Added ' + product.name + ' to card.';
  }

  startEditing() {
    this.selectedCategoryCode = this.product.value.category.code;
    this.isEditing = true;
  }

  canEdit() {
    return this.loginService.isModerator;
  }

  patch() {
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

  onLinksSubmit() {
    this.submitted = true;
    if (!this.linksForm.valid) {
      return;
    }
    const form = this.linksForm.value;
    const map: Map<string, string> = new Map<string, string>();
    Object.keys(this.product.value.dataLinks).forEach(key => {
      map.set(key, this.product.value.dataLinks[key]);
    });
    map.set(form.name, form.link);
    this.productService.patch(this.product.value.code, {
      dataLinks: this.productService.mapToObj(map)
    } as Product).subscribe(res => {
      this.loadProduct();
      this.isEditing = false;
    });
  }

  get f() {
    return this.linksForm.controls;
  }

  mapNotEmpty() {
    return this.product.value.dataLinks && Object.keys(this.product.value.dataLinks).length > 0;
  }

  deleteLink(key: string) {
    delete this.product.value.dataLinks[key];
    this.productService.patch(this.product.value.code, {
      dataLinks: this.product.value.dataLinks
    } as Product).subscribe(res => {
      this.loadProduct();
    });
  }
}
