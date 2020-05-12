import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';



interface PageAmount{
  amount: number;
};

@Component({
  selector: 'app-page-select',
  templateUrl: './page-select.component.html',
  styleUrls: ['./page-select.component.scss']
})
export class PageSelectComponent implements OnInit {

  @Input("amountOfPages")
  amountOfPages: BehaviorSubject<number>;

  @Input("selectedPage")
  selectedPage: BehaviorSubject<number>;

  @Output("onPageSelect")
  onPageSelect= new EventEmitter<number>();

  constructor() {}

  ngOnInit(): void {
  }

  pageSelect(pageNumber: any){
    if(this.selectedPage.value === pageNumber){
      return;
    }
    this.onPageSelect.next(pageNumber.pageIndex as number);
  }

}
