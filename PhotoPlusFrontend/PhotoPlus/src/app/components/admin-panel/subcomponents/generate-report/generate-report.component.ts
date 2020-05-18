import { Component, OnInit, ViewChild, ElementRef, Renderer2 } from '@angular/core';
import { ReportService } from 'src/app/services/report/report.service';
import { BehaviorSubject } from 'rxjs';
import { Product } from 'src/app/models/product/product';
import { ProductService } from 'src/app/services/product/product.service';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-generate-report',
  templateUrl: './generate-report.component.html',
  styleUrls: ['./generate-report.component.scss']
})
export class GenerateReportComponent implements OnInit {

  @ViewChild("fromDateProfit",{static: true})
  fromDateElProfit: ElementRef;

  @ViewChild("toDateProfit",{static: true})
  toDateElProfit: ElementRef;

  @ViewChild("fromDateProduct",{static: true})
  fromDateElProduct: ElementRef;

  @ViewChild("toDateProduct",{static: true})
  toDateElProduct: ElementRef;

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
        x.value.code.toLowerCase().includes(searchText.toLowerCase()) || x.value.name.toLowerCase().includes(searchText.toLowerCase())
      );
    })
    //no polish locale due to another angular issue
    //using en-EN with gmt +1
    const today = formatDate(new Date(), 'yyyy-MM-dd', 'en-EN', '+0100');
    this.toDateElProduct.nativeElement.value = today;
    this.toDateElProfit.nativeElement.value = today;
    this.fromDateElProduct.nativeElement.value = today;
    this.fromDateElProfit.nativeElement.value = today;
  }
  generateProfitReport(){
    const fromDate = this.fromDateElProfit.nativeElement.value as Date;
    const toDate = this.toDateElProfit.nativeElement.value as Date;
    if(fromDate > toDate){
      alert("Starting date should not be greater than ending date.");
      return;
    }
    this.reportService.getProfitReport(fromDate, toDate).subscribe(pdfObject => {
      this.createAndDownloadData(pdfObject,`${fromDate}_${toDate}_profit_report.pdf`);
    })
  }
  generateProductReport(){
    const fromDate = this.fromDateElProduct.nativeElement.value as Date;
    const toDate = this.toDateElProduct.nativeElement.value as Date;
    const productCode = this.productEl.nativeElement.value as string;
    if(fromDate > toDate){
      alert("Starting date should not be greater than ending date.");
      return;
    }
    if(productCode != null && !productCode){
      alert("Please select product.");
      return;
    }
    this.reportService.getProductReport(productCode, fromDate, toDate).subscribe(pdfObject => {
      this.createAndDownloadData(pdfObject, `from_${fromDate}_to_${toDate}_${productCode}_product_report.pdf`)
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
