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
import { ProductService } from "src/app/services/product/product.service";
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
  filteredProducts: BehaviorSubject<Product>[];

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
    this.getAllProducts();
    this.setupBatchForm();
    this.setupSearchBar();
  }

  getAllProducts() {
    this.products = new Array();
    this.filteredProducts = new Array();
    this.selectedProduct = new BehaviorSubject(null);
    this.productService.getAll().subscribe((products) => {
      for (const product of products) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
      this.filteredProducts = this.products;
    });
  }

  setupBatchForm() {
    this.batchForm = this.formBuilder.group({
      purchasePrice: [
        "",
        [Validators.required, Validators.pattern("([0-9]+(.|,)[0-9]+)")],
      ],
      date: ["", [Validators.required]],
      supplyQuantity: ["", [Validators.required, Validators.pattern("[0-9]+")]],
      storeQuantity: ["", [Validators.pattern("[0-9]+")]],
    });
  }

  setupSearchBar() {
    this.renderer.listen(this.searchBarEl.nativeElement, "input", () => {
      const searchText:string = this.searchBarEl.nativeElement.value;
      
      if(searchText === ''){
        this.filteredProducts = this.products;
      } else{
        this.filteredProducts = this.products.filter((x) => {
          return x.value.code.includes(searchText) || x.value.name.toLowerCase().includes(searchText.toLowerCase());
        });
      }
    });
  }

  selectProduct(code: string) {
    const selectedProduct = this.products.filter((x) => {
      return x.value.code === code;
    })[0];
    this.selectedProduct.next(selectedProduct.value);
  }

  onSubmit() {
    this.submitted = true;
    console.log(this.batchForm);
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

  get f() {
    return this.batchForm.controls;
  }
}
