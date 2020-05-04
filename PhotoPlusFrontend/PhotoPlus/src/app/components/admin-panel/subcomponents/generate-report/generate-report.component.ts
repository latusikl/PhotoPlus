import { Component, OnInit, ViewChild, ElementRef, Renderer2 } from '@angular/core';
import { ReportService } from 'src/app/services/report/report.service';
import { BehaviorSubject } from 'rxjs';
import { Product } from 'src/app/models/product/product';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-generate-report',
  templateUrl: './generate-report.component.html',
  styleUrls: ['./generate-report.component.scss']
})
export class GenerateReportComponent implements OnInit {

  @ViewChild("fromDate",{static: true})
  fromDateEl: ElementRef;

  @ViewChild("toDate",{static: true})
  toDateEl: ElementRef;

  @ViewChild("productCode", {static: true})
  productEl: ElementRef;

  products: BehaviorSubject<Product>[];
  filteredProducts: BehaviorSubject<Product>[];

  constructor(private reportService: ReportService, private productService: ProductService, private renderer:Renderer2) { }

  ngOnInit(): void {
    this.products = new Array<BehaviorSubject<Product>>();
    this.filteredProducts = new Array<BehaviorSubject<Product>>();
    this.productService.getAll().subscribe(data => {
      for(let product of data){
        this.productService.getDataFromLinks(product);
        this.products.push(new BehaviorSubject(product))
      }
      this.filteredProducts = this.products;
    })
    this.renderer.listen(this.productEl.nativeElement, "input", () => {
      const searchText = this.productEl.nativeElement.value;
      if(searchText == ''){
        this.filteredProducts = this.products;
        return;
      }
      this.filteredProducts = this.products.filter((x) => 
        x.value.code.toString().toLowerCase().includes(searchText.toLowerCase()) || x.value.name.toLowerCase().includes(searchText.toLowerCase())
      );
    })
  }
  generateProfitReport(){
    const fromDate = this.fromDateEl.nativeElement.value as Date;
    const toDate = this.toDateEl.nativeElement.value as Date;
    console.log(fromDate, toDate, fromDate > toDate);
    if(fromDate > toDate){
      alert("Starting date should not be greater than ending date");
      return;
    }
    this.reportService.getProfitReport(fromDate, toDate).subscribe(pdfObject => {
      this.createAndDownloadData(pdfObject,`${fromDate}_${toDate}_profit_report.pdf`);
    })
  }
  generateProductReport(){
    const productCode = this.productEl.nativeElement.value as string;
    console.log(productCode);
    this.reportService.getProductReport(productCode).subscribe(pdfObject => {
      this.createAndDownloadData(pdfObject, `${new Date()}_${productCode}_product_report.pdf`)
    })
    
  }

  insertCodeToSearchBar(product: BehaviorSubject<Product>){
    this.productEl.nativeElement.value = product.value.code;
  }

  createAndDownloadData(data: Blob, filename: string){
    const downloadURL = window.URL.createObjectURL(data);
      const link = document.createElement("a");
      link.href = downloadURL;
      link.download = filename;
      link.click();
  }
}
