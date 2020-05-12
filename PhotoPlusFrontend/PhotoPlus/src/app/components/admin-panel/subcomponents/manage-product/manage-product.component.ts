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
import { ProductService } from "src/app/services/product/product.service";
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
  filteredProducts: BehaviorSubject<Product>[];

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
    this.loadProducts();
    this.loadCategories();
    this.createForm();
    this.setupSearchBarListener();
  }

  loadProducts(completionHandler?: Function) {
    this.products = new Array<BehaviorSubject<Product>>();
    this.filteredProducts = new Array<BehaviorSubject<Product>>();
    this.productService.getAll().subscribe((products) => {
      for (let product of products) {
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product));
      }
      this.filteredProducts = this.products;
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
    const isDeleted = confirm(
      `Should this category be deleted?\n Code:${category.value.code} Name:${category.value.name}`
    );

    if (isDeleted) {
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

  setupSearchBarListener() {
    this.renderer.listen(this.searchBar.nativeElement, "input", () => {
      const searchText = this.searchBar.nativeElement.value;
      if (searchText == "") {
        this.filteredProducts = this.products;
        return;
      }
      this.filterDisplayedProducts(searchText);
    });
  }

  forceFilterDisplayedProducts() {
    const searchText = this.searchBar.nativeElement.value;
    this.filterDisplayedProducts(searchText);
  }

  filterDisplayedProducts(searchText: string) {
    this.filteredProducts = this.products.filter(
      (x) =>
        x.value.code
          .toLowerCase()
          .includes(searchText.toLowerCase()) ||
        x.value.name.toLowerCase().includes(searchText.toLowerCase())
    );
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
      category: form.productCategory,
      price: form.productPrice,
      imageCodes: [],
      storeQuantity: 0,
      code: null
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
          this.loadProducts(() => {
            const newProductSelection = this.products.find(
              (x) => x.value.code === this.selectedProduct.value.code
            );
            if (!newProductSelection) {
              return;
            }
            this.selectedProduct.next(newProductSelection.value);
          });
        });
    });
  }

  deleteProduct(code: string) {
    if (confirm("Do you want to delete this product?")) {
      this.productService.delete(code).subscribe(() => {
        this.products = this.products.filter((x) => {
          return x.value.code !== code;
        });
        this.forceFilterDisplayedProducts();
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
          this.imageService.post(photoFile).subscribe((photoResponse) => {
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

  patchImageCodes(imageCodeArray: string[]) {
    this.productService
      .patch(this.selectedProduct.value.code, {
        imageCodes: imageCodeArray,
      } as Product)
      .subscribe(() => {
        this.loadProducts(() => {
          const newProductSelection = this.products.find(
            (x) => x.value.code === this.selectedProduct.value.code
          );
          if (!newProductSelection) {
            return;
          }
          this.selectedProduct.next(newProductSelection.value);
        });
      });
  }

  get f() {
    return this.productCreationForm.controls;
  }
}
