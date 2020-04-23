import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionRemoveComponent } from './section-remove.component';

describe('SectionRemoveComponent', () => {
  let component: SectionRemoveComponent;
  let fixture: ComponentFixture<SectionRemoveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SectionRemoveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SectionRemoveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
