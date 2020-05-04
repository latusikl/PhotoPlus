import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePrivilegesComponent } from './change-privileges.component';

describe('ChangePrivilegesComponent', () => {
  let component: ChangePrivilegesComponent;
  let fixture: ComponentFixture<ChangePrivilegesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangePrivilegesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangePrivilegesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
