import { TestBed } from '@angular/core/testing';

import { WrappingService } from './wrapping.service';

describe('WrappingService', () => {
  let service: WrappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WrappingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
