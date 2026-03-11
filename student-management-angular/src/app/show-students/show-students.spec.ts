import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowStudents } from './show-students';

describe('ShowStudents', () => {
  let component: ShowStudents;
  let fixture: ComponentFixture<ShowStudents>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowStudents],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowStudents);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
