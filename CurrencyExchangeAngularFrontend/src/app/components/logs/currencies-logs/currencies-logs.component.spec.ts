import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrenciesLogsComponent } from './currencies-logs.component';

describe('CurrenciesLogsPageComponent', () => {
  let component: CurrenciesLogsComponent;
  let fixture: ComponentFixture<CurrenciesLogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrenciesLogsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrenciesLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
