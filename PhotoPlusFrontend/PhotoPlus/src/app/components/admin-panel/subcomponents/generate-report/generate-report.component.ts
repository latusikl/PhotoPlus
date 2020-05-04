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

  constructor(private reportService: ReportService) { }

  ngOnInit(): void {
    

  }
  generateProfitReport(){
    const fromDate = this.fromDateEl.nativeElement.value as Date;
    const toDate = this.toDateEl.nativeElement.value as Date;
    console.log(fromDate, toDate, fromDate > toDate);
    if(fromDate > toDate ){
      alert("Starting date should not be greater than ending date");
      return;
    }
    this.reportService.getProfitReport(fromDate, toDate).subscribe(pdfObject => {
      const downloadURL = window.URL.createObjectURL(pdfObject);
      const link = document.createElement("a");
      link.href = downloadURL;
      link.download = `${fromDate} ${toDate} profit report.pdf`;
      link.click();
    })
  }
}
