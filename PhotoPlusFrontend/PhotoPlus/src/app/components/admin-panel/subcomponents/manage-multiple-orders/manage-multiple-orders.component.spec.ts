import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageMultipleOrdersComponent } from './manage-multiple-orders.component';

describe('ManageOrdersComponent', () => {
  let component: ManageMultipleOrdersComponent;
  let fixture: ComponentFixture<ManageMultipleOrdersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageMultipleOrdersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageMultipleOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
