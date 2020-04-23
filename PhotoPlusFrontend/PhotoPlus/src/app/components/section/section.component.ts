import { Component, OnInit, Input } from '@angular/core';
import { Section } from 'src/app/models/section/section';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.scss']
})
export class SectionComponent implements OnInit {

  @Input("section")
  section: Section;

  constructor() { }

  ngOnInit(): void {
  }

}
