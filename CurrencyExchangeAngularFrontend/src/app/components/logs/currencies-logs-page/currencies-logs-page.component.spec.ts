import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrenciesLogsPageComponent } from './currencies-logs-page.component';

describe('CurrenciesLogsPageComponent', () => {
  let component: CurrenciesLogsPageComponent;
  let fixture: ComponentFixture<CurrenciesLogsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrenciesLogsPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrenciesLogsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
