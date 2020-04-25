import { Component, OnInit, Input } from '@angular/core';
import { Section } from 'src/app/models/section/section';
import { ActivatedRoute, Router } from '@angular/router';
import { SectionService } from 'src/app/services/section/section.service';
import { async } from '@angular/core/testing';

@Component({
  selector: 'app-section-remove',
  templateUrl: './section-remove.component.html',
  styleUrls: ['./section-remove.component.scss']
})
export class SectionRemoveComponent implements OnInit {

  section: Section;

  constructor(private activatedRoute:ActivatedRoute, private sectionService:SectionService, private router:Router) { 
    this.section = new Section();
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let sectionCode = params['code'];
      
      this.sectionService.getSingle(sectionCode).subscribe( (data:Section) => {
        this.section = data;
      })
    })
  }

  deleteSection(){
    this.sectionService.delete(this.section.code).subscribe((data:Section)=>{
      console.log(data);
      this.router.navigate(['/forum']);
    })
  }

  takeMeBack(){
    this.router.navigate(['/forum']);
  }

}
