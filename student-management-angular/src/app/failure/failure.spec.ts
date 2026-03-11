import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Failure } from './failure';

describe('Failure', () => {
  let component: Failure;
  let fixture: ComponentFixture<Failure>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Failure],
    }).compileComponents();

    fixture = TestBed.createComponent(Failure);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
