import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffMenu } from './staff-menu';

describe('StaffMenu', () => {
  let component: StaffMenu;
  let fixture: ComponentFixture<StaffMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StaffMenu],
    }).compileComponents();

    fixture = TestBed.createComponent(StaffMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
