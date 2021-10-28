import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeRatesInfoComponent } from './exchange-rates-info.component';

describe('ExchangeRatesInfoComponent', () => {
  let component: ExchangeRatesInfoComponent;
  let fixture: ComponentFixture<ExchangeRatesInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExchangeRatesInfoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExchangeRatesInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
