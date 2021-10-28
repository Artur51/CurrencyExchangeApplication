import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogsPaginationComponent } from './logs-pagination.component';

describe('LogsPaginationComponent', () => {
  let component: LogsPaginationComponent;
  let fixture: ComponentFixture<LogsPaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LogsPaginationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LogsPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
