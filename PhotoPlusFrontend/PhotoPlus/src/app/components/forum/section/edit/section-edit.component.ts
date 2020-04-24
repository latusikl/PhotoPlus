import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SectionService } from 'src/app/services/section/section.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Section } from 'src/app/models/section/section';

@Component({
  selector: 'app-section-edit',
  templateUrl: './section-edit.component.html',
  styleUrls: ['./section-edit.component.scss']
})
export class SectionEditComponent implements OnInit {

  section:Section;
  sectionForm:FormGroup;
  submitted:boolean;

  constructor(private activatedRoute:ActivatedRoute,private formBuilder: FormBuilder,private sectionService:SectionService, private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params =>{
      let sectionCode = params["code"];
      this.sectionService.getSingle(sectionCode).subscribe((data:Section)=>{
        this.section = data;
        this.sectionForm = this.formBuilder.group({
          name: [this.section.name, Validators.required],
          description: [this.section.description],
        });
      })
    })

  }

  get f(){
    return this.sectionForm.controls;
  }

  onSubmit(){
    this.submitted = true;
    if(this.sectionForm.invalid){
      return;
    }
    const form = this.sectionForm.value;
    this.sectionService.patch(parseInt(this.section.code), {
      ...this.section,
      ...form
    }).subscribe((data) => {
      console.log(data)
      this.router.navigate(['/forum']);
    });
  }
}
