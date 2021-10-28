import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppNumericRangeComponent } from './app-numeric-range.component';

describe('AppNumericRangeComponent', () => {
  let component: AppNumericRangeComponent;
  let fixture: ComponentFixture<AppNumericRangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AppNumericRangeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppNumericRangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
