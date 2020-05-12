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

  @Output("onPageSelect")
  onPageSelect= new EventEmitter<number>();

  constructor() {}

  ngOnInit(): void {
  }

  get generateRange(){
    const captureAmount = this.amountOfPages.value;
    return Array.from(
      (function*(){
          let i = 1;
          while(i <= captureAmount) 
            yield i++;
        }()
      )
    );
  }

  pageSelect(pageNumber: number){
    this.onPageSelect.next(pageNumber);
  }

}
