import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicBodyComponent } from './topic-body.component';

describe('TopicBodyComponent', () => {
  let component: TopicBodyComponent;
  let fixture: ComponentFixture<TopicBodyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopicBodyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicBodyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
