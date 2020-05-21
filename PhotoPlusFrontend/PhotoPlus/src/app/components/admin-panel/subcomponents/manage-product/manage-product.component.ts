import {
  Component,
  OnInit,
  Renderer2,
  ViewChild,
  ElementRef,
  ViewContainerRef,
} from "@angular/core";
import { CategoryService } from "src/app/services/category/category.service";
import { Category } from "src/app/models/category/category";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Product } from "src/app/models/product/product";
import { ProductService, ProductSortBy } from "src/app/services/product/product.service";
import { BehaviorSubject } from "rxjs";
import { ImageService } from "src/app/services/image/image.service";

@Component({
  selector: "app-create-new-product",
  templateUrl: "./manage-product.component.html",
  styleUrls: ["./manage-product.component.scss"],
})
export class ManageProductComponent implements OnInit {
  @ViewChild("searchBar", { static: true })
  searchBar: ElementRef;

  @ViewChild("inputDialog")
  imageInputDialog: ElementRef;

  @ViewChild("photoDisplay")
  photoDisplay: ViewContainerRef;

  selectedProduct: BehaviorSubject<Product>;

  productCreationForm: FormGroup;
  submitted: boolean;

  products: BehaviorSubject<Product>[];

  amountOfPages: BehaviorSubject<number>;
  currentPage:BehaviorSubject<number>;

  howMuchMilisecsBeforeFetch = 500;
  searchBarInputTimer: NodeJS.Timeout;

  shouldSearch:boolean = false;
  searchPhrase:string;
  

  categories: BehaviorSubject<Category>[];

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private renderer: Renderer2,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
    this.selectedProduct = new BehaviorSubject(null);
    this.currentPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    this.loadProductsPageInfo();
    this.loadProducts();
    this.loadCategories();
    this.createForm();
    this.setupSearchBarListener();
  }


  async loadSearchedPageInfo(){
    const pageInfo = this.productService.getAllProductsSearchedPageInfo(this.searchPhrase).toPromise();
    this.currentPage.next(0);
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  loadSearchedProducts(completionHandler?: Function){
    this.productService.getAllProductsSearchedByPage(this.currentPage.value, this.searchPhrase).subscribe((data) =>{
      this.products = new Array();
      for(let product of data){
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
      if(completionHandler){
        completionHandler();
      }
    })
  }

  async loadProductsPageInfo(){
    const pageInfo = this.productService.getPageCount().toPromise();
    this.amountOfPages.next((await pageInfo).pageAmount);
  }

  async loadProducts(completionHandler?: Function) {
    this.products = new Array<BehaviorSubject<Product>>();
    this.productService.getPage(this.currentPage.value).subscribe((products) => {
      for (let product of products) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
      if (completionHandler) {
        completionHandler();
      }
    });
  }

  loadCategories() {
    this.categories = new Array<BehaviorSubject<Category>>();
    this.categoryService.getAll().subscribe((categories) => {
      for (let category of categories) {
        this.categories.push(new BehaviorSubject<Category>(category));
      }
    });
  }

  addCategory() {
    const newCategoryName = prompt("Type created category name:");

    if (newCategoryName) {
      this.categoryService
        .post({ code: null, name: newCategoryName })
        .subscribe(() => {
          this.categoryService.getAll().subscribe((categories) => {
            this.categories = new Array<BehaviorSubject<Category>>();
            for (const category of categories) {
              this.categories.push(new BehaviorSubject(category));
            }
          });
        });
    }
  }

  editCategory(code: string) {
    const category = this.findCategory(code);
    const categoryNewName = prompt(
      "Type category new name",
      category.value.name
    );

    if (categoryNewName) {
      const patchCategory: Category = { code: code, name: categoryNewName };
      this.categoryService
        .patch(code, patchCategory)
        .subscribe(() => {
          category.next(patchCategory);
        });
    }
  }

  findCategory(code: string): BehaviorSubject<Category> {
    return this.categories.find((x) => {
      return x.value.code === code;
    });
  }

  deleteCategory(code: string) {
    const category = this.findCategory(code);
    const shouldBeDeleted = confirm(
      `Should this category be deleted?\n Code:${category.value.code} Name:${category.value.name}`
    );

    if (shouldBeDeleted) {
      this.categoryService.delete(category.value.code).subscribe(() => {
        this.categories = this.categories.filter((x) => {
          return x.value.code !== code;
        });
      });
    }
  }

  createForm() {
    this.productCreationForm = this.formBuilder.group({
      productName: ["", [Validators.required]],
      productDescription: ["", []],
      productCategory: ["", [Validators.required]],
      productPrice: [
        "0.00",
        [Validators.required, Validators.pattern(/^[0-9]+([.][0-9]{0,2})?$/)],
      ],
    });
  }

  async setupSearchBarListener() {
    this.renderer.listen(this.searchBar.nativeElement, "input", () => {
      this.searchPhrase = this.searchBar.nativeElement.value;
      clearTimeout(this.searchBarInputTimer);
      if (this.searchPhrase == "") {
        this.shouldSearch = false;
        this.loadProductsPageInfo();
        this.loadProducts();
        return;
      }
      if(this.searchPhrase.length > 2){
        this.searchBarInputTimer = setTimeout(async () => {
          this.shouldSearch = true;
          await this.loadSearchedPageInfo();
          await this.loadSearchedProducts();
        }, this.howMuchMilisecsBeforeFetch);
      }
    });
  }

  chooseProduct(productCode: string) {
    const singleProduct = this.products.filter(
      (x) => x.value.code === productCode
    );
    this.selectedProduct.next(singleProduct[0]?.value);
  }

  onSubmit() {
    this.submitted = true;
    if (!this.productCreationForm.valid) {
      return;
    }
    const form = this.productCreationForm.value;

    const product: Product = {
      name: form.productName,
      description: form.productDescription,
      categoryCode: form.productCategory,
      price: form.productPrice,
      imageCodes: [],
      storeQuantity: 0,
      code: null,
      category: null
    };
    this.productService.post(product).subscribe(() => {
      this.loadProducts();
      alert("Success");
    });
  }

  goBack() {
    this.selectedProduct.next(null);
  }

  addPhoto() {
    const e: HTMLElement = this.imageInputDialog.nativeElement;
    e.click();
  }

  deletePhoto(code: string) {
    this.imageService.delete(code).subscribe(() => {
      this.productService
        .getSingle(this.selectedProduct.value.code)
        .subscribe(() => {
          this.reloadProducts();
        });
    });
  }

  deleteProduct(code: string) {
    if (confirm("Do you want to delete this product?")) {
      this.productService.delete(code).subscribe(() => {
        this.products = this.products.filter((x) => {
          return x.value.code !== code;
        });
        this.reloadProducts();
        this.goBack();
      });
    }
  }

  findProduct(code: string): BehaviorSubject<Product> {
    return this.products.filter((x) => {
      return x.value.code !== code;
    })[0];
  }

  sendFile() {
    const photoPicutresToUpload = this.imageInputDialog.nativeElement;
    let imageCodes = [];
    let currentAmount = 0;

    this.productService
      .getSingle(this.selectedProduct.value.code)
      .subscribe((currentProduct) => {
        if (currentProduct.imageCodes) {
          imageCodes = currentProduct.imageCodes;
        }
        for (let photoFile of photoPicutresToUpload.files) {
          this.imageService.post(photoFile, currentProduct.code).subscribe((photoResponse) => {
            const newImageCode = photoResponse.headers.get("Entity-Code");
            imageCodes.push(newImageCode);
            currentAmount++;
            if (currentAmount === photoPicutresToUpload.files.length) {
              this.patchImageCodes(imageCodes);
            }
          });
        }
      });
  }

  async patchImageCodes(imageCodeArray: string[]) {
    this.productService
      .patch(this.selectedProduct.value.code, {
        imageCodes: imageCodeArray
      } as Product)
      .subscribe(() => {
        this.reloadProducts();
      });
  }

  reloadProducts(){
    if(this.shouldSearch){
      this.loadSearchedProducts(()=> {
        this.reloadProductsNotCheckingIfSearched();
      })
    } else{
      this.loadProducts(()=>{
        this.reloadProductsNotCheckingIfSearched();
      })
    }

  }

  private reloadProductsNotCheckingIfSearched(){
    const newProductSelection = this.products.find(
      (x) => x.value.code === this.selectedProduct.value.code
    );
    if (!newProductSelection) {
      return;
    }
    this.selectedProduct.next(newProductSelection.value);
  }

  changePage(nextPage: number){
    this.currentPage.next(nextPage);
    if(this.shouldSearch){
      this.loadSearchedProducts();
    }else{
      this.loadProducts();
    }
  }    

  get f() {
    return this.productCreationForm.controls;
  }
}
