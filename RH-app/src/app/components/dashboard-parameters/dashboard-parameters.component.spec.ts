import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardParametersComponent } from './dashboard-parameters.component';

describe('DashboardParametersComponent', () => {
  let component: DashboardParametersComponent;
  let fixture: ComponentFixture<DashboardParametersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardParametersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardParametersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
