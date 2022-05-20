import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeeklyTimesheetComponent } from './weekly-timesheet.component';

describe('WeeklyTimesheetComponent', () => {
  let component: WeeklyTimesheetComponent;
  let fixture: ComponentFixture<WeeklyTimesheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WeeklyTimesheetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeeklyTimesheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
