import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-manage-orders',
  templateUrl: './manage-multiple-orders.component.html',
  styleUrls: ['./manage-multiple-orders.component.scss']
})
export class ManageMultipleOrdersComponent implements OnInit {

  orderStatuses: string[] = [
    'Pending',
    'Paid',
    'Ready to ship',
    'Shipped',
    'Delivered'
  ]


  constructor() { }

  ngOnInit(): void {
  }

}
