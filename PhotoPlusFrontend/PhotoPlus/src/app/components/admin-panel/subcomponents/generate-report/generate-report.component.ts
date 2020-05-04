import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ReportService } from 'src/app/services/report/report.service';

@Component({
  selector: 'app-generate-report',
  templateUrl: './generate-report.component.html',
  styleUrls: ['./generate-report.component.scss']
})
export class GenerateReportComponent implements OnInit {

  @ViewChild("fromDate")
  fromDateEl: ElementRef;

  @ViewChild("toDate")
  toDateEl: ElementRef;

  @ViewChild("productCode")
  productCodeEl: ElementRef;

  constructor(private reportService: ReportService) { }

  ngOnInit(): void {
    

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
    const productCode = this.productCodeEl.nativeElement.value as string;
    console.log(productCode);
    this.reportService.getProductReport(productCode).subscribe(pdfObject => {
      this.createAndDownloadData(pdfObject, `${new Date()}_${productCode}_product_report.pdf`)
    })
    
  }
  createAndDownloadData(data: Blob, filename: string){
    const downloadURL = window.URL.createObjectURL(data);
      const link = document.createElement("a");
      link.href = downloadURL;
      link.download = filename;
      link.click();
  }
}
