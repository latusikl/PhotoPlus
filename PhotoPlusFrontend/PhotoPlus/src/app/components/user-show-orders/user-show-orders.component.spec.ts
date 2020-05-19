import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserShowOrdersComponent } from './user-show-orders.component';

describe('UserShowOrdersComponent', () => {
  let component: UserShowOrdersComponent;
  let fixture: ComponentFixture<UserShowOrdersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [UserShowOrdersComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserShowOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
