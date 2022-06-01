import { TestBed } from '@angular/core/testing';

import { timesheetService } from './timesheet.service';

describe('TimesheetService', () => {
  let service: timesheetService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(timesheetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
