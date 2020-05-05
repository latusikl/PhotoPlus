import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageSingleOrderComponent } from './manage-single-order.component';

describe('ManageSingleOrderComponent', () => {
  let component: ManageSingleOrderComponent;
  let fixture: ComponentFixture<ManageSingleOrderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageSingleOrderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageSingleOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
