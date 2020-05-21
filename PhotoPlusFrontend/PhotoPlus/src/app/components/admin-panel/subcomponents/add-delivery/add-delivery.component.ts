import {
  Component,
  OnInit,
  Renderer2,
  ViewChild,
  ElementRef,
} from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { BatchService } from "src/app/services/batch/batch.service";
import { BehaviorSubject } from "rxjs";
import { Product } from "src/app/models/product/product";
import { ProductSortBy, ProductService } from "src/app/services/product/product.service";
import { Batch } from "src/app/models/batch/batch";

@Component({
  selector: "app-add-delivery",
  templateUrl: "./add-delivery.component.html",
  styleUrls: ["./add-delivery.component.scss"],
})
export class AddDeliveryComponent implements OnInit {
  @ViewChild("searchBar", { static: true })
  searchBarEl: ElementRef;

  products: BehaviorSubject<Product>[];
  amountOfPages: BehaviorSubject<number>;
  currentPage:BehaviorSubject<number>;

  howMuchMilisecsBeforeFetch = 500;
  searchBarInputTimer: NodeJS.Timeout;

  shouldSearch:boolean = false;
  searchPhrase:string;

  batchForm: FormGroup;
  submitted: boolean;

  selectedProduct: BehaviorSubject<Product>;

  constructor(
    private formBuilder: FormBuilder,
    private batchService: BatchService,
    private productService: ProductService,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    this.currentPage = new BehaviorSubject(0);
    this.amountOfPages = new BehaviorSubject(0);
    this.selectedProduct = new BehaviorSubject(null);
    this.setupBatchForm();
    this.loadProducts();
    this.setupSearchBar();
  }


  setupBatchForm() {
    this.batchForm = this.formBuilder.group({
      purchasePrice: [
        "",
        [Validators.required, Validators.pattern(/^[0-9]+([.][0-9]{0,2})?$/)],
      ],
      date: ["", [Validators.required]],
      supplyQuantity: ["", [Validators.required, Validators.pattern("[0-9]+")]],
      storeQuantity: ["", [Validators.pattern("[0-9]+")]],
    });
  }

  async loadProducts(){
    const pageInfo = this.productService.getPageCount().toPromise();
    this.amountOfPages.next((await pageInfo).pageAmount);
    if(await (await pageInfo).pageAmount === 0){
      return;
    }
    this.productService.getPage(this.currentPage.value).subscribe(data => {
      this.products = new Array();
      for(let product of data){
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    })
  }

  loadSearchedProductsPageInfo(){
    this.productService.getAllProductsSearchedPageInfo(this.searchPhrase).subscribe( data => {
      this.currentPage.next(0);
      this.amountOfPages.next(data.pageAmount);
    });
  }

  async loadSearchedProducts(){
      this.productService.getAllProductsSearchedByPage(this.currentPage.value, this.searchPhrase).subscribe(data =>{
      this.products = new Array();
      for(const product of data){
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
    });
  }



  setupSearchBar() {
    this.renderer.listen(this.searchBarEl.nativeElement, "input", () => {
      clearTimeout(this.searchBarInputTimer);
      this.searchPhrase = this.searchBarEl.nativeElement.value;
      if(this.searchPhrase === ''){
        this.loadProducts();
        this.shouldSearch = false;
        return;
      }
      if(this.searchPhrase.length > 2){
        this.shouldSearch = true;
        this.searchBarInputTimer = setTimeout(()=> {
          this.loadSearchedProductsPageInfo();
          this.loadSearchedProducts();
        },this.howMuchMilisecsBeforeFetch);
      }     
    });
  }


  selectProduct(code: string) {
    const selectedProducts = this.products.filter((x) => {
      return x.value.code === code;
    });
    this.selectedProduct.next(selectedProducts[0]?.value);
  }

  onSubmit() {
    this.submitted = true;
    if (!this.batchForm.valid) {
      return;
    }
    if (!this.selectedProduct.value) {
      alert("Select product to add");
      return;
    }
    const form = this.batchForm.value;
    const newBatch: Batch = {
      productCode: this.selectedProduct.value.code,
      date: form.date,
      purchasePrice: form.purchasePrice,
      supplyQuantity: form.supplyQuantity,
      storeQuantity: form.storeQuantity || form.supplyQuantity,
    };
    this.batchService.post(newBatch).subscribe(() => {
      alert("New batch added!");
    })
  }

  changePage(pageNumber: number){
    this.currentPage.next(pageNumber);
    if(this.shouldSearch){
      this.loadSearchedProducts();
    }else{
      this.loadProducts();
    }
  }

  get f() {
    return this.batchForm.controls;
  }
}
