import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionAddComponent } from './section-add.component';

describe('SectionAddComponent', () => {
  let component: SectionAddComponent;
  let fixture: ComponentFixture<SectionAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SectionAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SectionAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
