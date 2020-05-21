import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ProductService } from '../../services/product/product.service';
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
import { Rating } from 'src/app/models/rating/rating';
import { DatePipe } from '@angular/common';
import { RatingService } from 'src/app/services/rating/rating.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
  providers: [DatePipe]
})
export class ProductComponent implements OnInit {

  @ViewChild('productName', { static: false })
  productNameTextArea: ElementRef;

  @ViewChild('rateContent', { static: false })
  rateContent: ElementRef;

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

  ratings: BehaviorSubject<Rating>[];
  sort = 'dateAsc';
  isStar = false;
  stars: number;
  avgPrice: BehaviorSubject<number>;

  content: any;
  myDate = new Date();
  selectedPage: number;
  amountOfPages: BehaviorSubject<number>;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private productService: ProductService,
              private cartService: CartService,
              private modalService: NgbModal,
              private loginService: LoginService,
              private categoryService: CategoryService,
              private formBuilder: FormBuilder,
              private datePipe: DatePipe,
              private ratingSerivce: RatingService
  ) { }

  async ngOnInit() {
    this.selectedPage = 0;
    this.amountOfPages = new BehaviorSubject(0);
    this.avgPrice = new BehaviorSubject(0);
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
    await this.loadRatingsPageInfo();
    this.loadRatings();
  }

  buy(product: Product) {
    if (!confirm('Are you sure you want to buy ' + product.name + '? \n This operation will clear your shopping cart.')) {
      return;
    }
    this.cartService.clearCart();
    this.cartService.addToCart(product);
    if (this.loginService.isLoggedIn() === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please login!';
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
      if (this.canEdit()) {
        this.productService.getAvgPurchasePrice(data.code).subscribe((avgPrice: number) => {
          this.avgPrice.next(avgPrice);
        })
      }
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

  FieldsChange(values: any) {
    this.isStar = true;
    this.stars = values.target.value;
  }

  changePage(page: number) {
    this.selectedPage = page;
    this.loadRatings();
  }

  onSortingChange() {
    this.loadRatings();
  }

  async loadRatingsPageInfo() {
    const pageInfo = this.ratingSerivce.getPageCountRating(this.param).toPromise();
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  async loadRatings() {
    this.ratingSerivce.getRatingsPage(this.selectedPage, this.sort, this.param).subscribe(data => {
      this.ratings = new Array();
      for (const rate of data) {
        this.ratings.push(new BehaviorSubject(rate));
      }
    });
  }

  rate() {
    if (this.loginService.isLoggedIn() === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please login!';
      return;
    }
    if (this.isStar === false) {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.title = 'Error occured!';
      modalRef.componentInstance.message = 'Please choose your rate!';
      return;
    }
    const rating = new Rating();
    rating.rate = this.stars;

    rating.productCode = this.param;
    rating.content = this.rateContent.nativeElement.value;
    rating.userLogin = this.loginService.getLoggedUser().login;
    rating.userCode = this.loginService.getLoggedUser().code;
    rating.date = this.datePipe.transform(this.myDate, 'yyyy-MM-dd');
    this.ratingSerivce.post(rating).subscribe(async data => {
      this.rateContent.nativeElement.value = '';
      this.isStar = false;
      this.deselectStars(); // ? Optional
      const modalRef = this.modalService.open(SuccessModalComponent);
      modalRef.componentInstance.title = 'Success!';
      modalRef.componentInstance.message = 'You rated product.';
      await this.loadRatingsPageInfo();
      this.loadRatings();
    });
  }

  deselectStars() {
    const stars = document.querySelectorAll('.all-stars');
    for (let i = 0; i < stars.length; i++) {
      (stars[i] as any).checked = false;
    }
  }

  get auth(): LoginService {
    return this.loginService;
  }

}
