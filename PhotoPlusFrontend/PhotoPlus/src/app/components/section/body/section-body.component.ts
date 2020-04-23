import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-section-body',
  templateUrl: './section-body.component.html',
  styleUrls: ['./section-body.component.scss']
})
export class SectionBodyComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      const sectionCode = params['code'];
      console.log(sectionCode);      
    })
  }

}
