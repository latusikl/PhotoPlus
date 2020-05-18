import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { BehaviorSubject } from 'rxjs';



interface PageAmount{
  amount: number;
};

@Component({
  selector: 'app-page-select',
  templateUrl: './page-select.component.html',
  styleUrls: ['./page-select.component.scss'],
})
export class PageSelectComponent implements OnInit {

  @Input('amountOfPages')
  amountOfPages: BehaviorSubject<number>;

  @Output('onPageSelect')
  onPageSelect= new EventEmitter<number>();

  constructor() {}

  ngOnInit(): void {
  }

  pageSelect(pageNumber: any){
    this.onPageSelect.next(pageNumber.pageIndex as number);
  }

}
