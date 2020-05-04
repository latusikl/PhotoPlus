import { Component, OnInit, Input } from '@angular/core';
import { Section } from 'src/app/models/section/section';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SectionService } from 'src/app/services/section/section.service';
import { Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-section-add',
  templateUrl: './section-add.component.html',
  styleUrls: ['./section-add.component.scss']
})
export class SectionAddComponent implements OnInit {

  sectionForm:FormGroup;
  submitted:boolean;

  constructor(private formBuilder: FormBuilder,private sectionService:SectionService, private router: Router) { }

  ngOnInit(): void {
    this.sectionForm = this.formBuilder.group({
      name: ['',Validators.required],
      description: [''],
    },);
  }

  get f() {
    return this.sectionForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if(this.sectionForm.invalid){
      return;
    }
    const form = this.sectionForm.value;
    this.sectionService.post({
      ...form
    }).subscribe(resp => {
      const keys = resp.headers.keys();
      let headers = keys.map(key =>
        `${key}: ${resp.headers.get(key)}`);

      console.log(headers)
      this.router.navigate(['/forum']);
    });
  }
}
