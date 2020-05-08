import {
  Component,
  OnInit,
  Renderer2,
  ViewChild,
  ElementRef,
  ComponentFactory,
  ComponentFactoryResolver,
  ViewContainerRef,
} from "@angular/core";
import { CategoryService } from "src/app/services/category/category.service";
import { Category } from "src/app/models/category/category";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Product } from "src/app/models/product/product";
import { ProductService } from "src/app/services/product/product.service";
import { BehaviorSubject, Subject } from 'rxjs';
import { ImageService } from 'src/app/services/image/image.service';
import { ImageCarouselComponent } from './image-carousel/image-carousel.component';

@Component({
  selector: "app-create-new-product",
  templateUrl: "./manage-product.component.html",
  styleUrls: ["./manage-product.component.scss"],
})
export class ManageProductComponent implements OnInit {

  @ViewChild("searchBar",{static: true})
  searchBar: ElementRef;

  @ViewChild("inputDialog")
  imageInputDialog: ElementRef;

  @ViewChild("photoDisplay")
  photoDisplay: ViewContainerRef;

  selectedProduct: BehaviorSubject<Product>;
  selectedProductImages: BehaviorSubject<string[]>;

  productCreationForm: FormGroup;
  submitted: boolean;

  products: BehaviorSubject<Product>[];
  filteredProducts: BehaviorSubject<Product>[];

  categories: BehaviorSubject<Category>[];

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private renderer: Renderer2,
    private imageService: ImageService,
    private componentFactoryResolver: ComponentFactoryResolver
  ) {}

  ngOnInit(): void {
    this.selectedProduct = new BehaviorSubject(null);
    this.loadProducts();
    this.loadCategories();
    this.createForm();
    this.setupSearchBarListener();
  }

  loadProducts(completionHandler?: Function){
    this.products = new Array<BehaviorSubject<Product>>();
    this.filteredProducts = new Array<BehaviorSubject<Product>>();
    this.selectedProductImages = new BehaviorSubject([]);
    this.productService.getAll().subscribe((products) => {
      for (let product of products) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
      this.filteredProducts = this.products;
      if(completionHandler){
        completionHandler();
      }
    });
  }

  loadCategories(){
    this.categories = new Array<BehaviorSubject<Category>>();
    this.categoryService.getAll().subscribe((categories) => {
      for (let category of categories) {
        this.categories.push(new BehaviorSubject<Category>(category));
      }
    });
  }

  createForm(){
    this.productCreationForm = this.formBuilder.group({
      productName: ["", [Validators.required]],
      productDescription: ["", []],
      productCategory: ["", [Validators.required]],
      productPrice: [
        "0.00",
        [Validators.required, Validators.pattern("([0-9]+(.|,)[0-9])")],
      ],
    });
  }

  setupSearchBarListener(){ 
    this.renderer.listen(this.searchBar.nativeElement, "input", () => {
      const searchText = this.searchBar.nativeElement.value;
      if (searchText == "") {
        this.filteredProducts = this.products;
        return;
      }
      this.filteredProducts = this.products.filter(
        (x) =>
          x.value.code
            .toString()
            .toLowerCase()
            .includes(searchText.toLowerCase()) ||
          x.value.name.toLowerCase().includes(searchText.toLowerCase())
      );
    });
  }

  chooseProduct(product: BehaviorSubject<Product>) {
    const productCode = product.value.code;
    const singleProduct = this.products.filter(
      (x) => x.value.code === productCode
    );
    if (!singleProduct[0].value) {
      return;
    }
    this.selectedProduct.next(singleProduct[0].value);
    this.updateSelectedProductPhotos();
  }

  updateSelectedProductPhotos(){
    this.selectedProductImages = new BehaviorSubject([]);
    for(let photo of this.selectedProduct.value.imagesUrl){
      this.selectedProductImages.value.push(photo);
    }
  }

  onSubmit() {
    this.submitted = true;
    if (!this.productCreationForm.valid) {
      const form = this.productCreationForm.value;
      const product: Product | any = {
        name: form.productName,
        description: form.productDescription,
        category: form.productCategory,
        price: form.productPrice,
        quantity: 0,
      };
      console.log(form.productCategory);
      this.productService.post(product).subscribe(()=>{
        alert("Success");
      })
    }
  }

  goBack() {
    this.selectedProduct.next(null);
  }

  addPhoto() {
    const e: HTMLElement = this.imageInputDialog.nativeElement;
    e.click();
  }

  sendFile(){
    const elem = this.imageInputDialog.nativeElement;
    let imageCodes = []
    let currentAmount = elem.files.length;
    this.productService.getSingle(this.selectedProduct.value.code).subscribe(productData => {
      if(productData.imageCodes){
        imageCodes = productData.imageCodes;
      }
      for(let file of elem.files){
        this.imageService.postImage(file).subscribe(imageResponse => {
          imageCodes.push(imageResponse.code);
          currentAmount++;
          this.updateOrCheckIfCanUpdate(currentAmount === elem.files.length, imageCodes);
        })
      }
    })
  }

  updateOrCheckIfCanUpdate(shouldUpdate: boolean, imageCodeArray: string[]){
    this.productService.patch(this.selectedProduct.value.code, {imageCodes: imageCodeArray} as Product).subscribe(() => {
      this.loadProducts(()=>{
        const newSelect = this.products.find((x)=> x.value.code === this.selectedProduct.value.code);
        console.log(newSelect);
        if(!newSelect){
          return;
        }
        this.selectedProduct.next(newSelect.value);
        this.photoDisplay.clear();
        console.log(this.photoDisplay);
        const factory = this.componentFactoryResolver.resolveComponentFactory(ImageCarouselComponent);
        const componentRef = this.photoDisplay.createComponent(factory);
        componentRef.instance.addPhoto = this.addPhoto;
        componentRef.instance.goBack = this.goBack;
        componentRef.instance.selectedProduct = this.selectedProduct;
      
      });
    })
  }

  get f() {
    return this.productCreationForm.controls;
  }
}
