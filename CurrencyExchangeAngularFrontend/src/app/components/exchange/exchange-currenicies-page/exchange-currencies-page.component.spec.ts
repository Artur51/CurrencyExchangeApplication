import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeCurrenciesPageComponent } from './exchange-currencies-page.component';

describe('ExchangeCurreniciesPageComponent', () => {
  let component: ExchangeCurrenciesPageComponent;
  let fixture: ComponentFixture<ExchangeCurrenciesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExchangeCurrenciesPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExchangeCurrenciesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
