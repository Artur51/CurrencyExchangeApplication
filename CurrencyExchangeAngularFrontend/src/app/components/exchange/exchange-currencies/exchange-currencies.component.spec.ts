import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeCurrenciesComponent } from './exchange-currencies.component';

describe('ExchangeCurrenciesComponent', () => {
  let component: ExchangeCurrenciesComponent;
  let fixture: ComponentFixture<ExchangeCurrenciesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExchangeCurrenciesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExchangeCurrenciesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
