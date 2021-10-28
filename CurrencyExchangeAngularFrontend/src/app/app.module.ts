import { MatStepperModule } from '@angular/material/stepper';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './routing/app-routing.module';
import { AppComponent } from './app.component';
import { CurrenciesLogsComponent } from './components/logs/currencies-logs/currencies-logs.component';
import { ExchangeCurrenciesPageComponent } from './components/exchange/exchange-currenicies-page/exchange-currencies-page.component';
import { LoginFormComponent } from './components/login/login-form/login-form.component';
import { NavbarComponent } from './components/common/navbar/navbar.component';
import { LogsPaginationComponent } from './components/logs/logs-pagination/logs-pagination.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { AuthLoginService } from './services/auth-login.service';
import { CurrencyExchangeDataService } from './services/currency-exchange-data.service';
import { Router } from '@angular/router';
import { RegisterFormComponent } from './components/login/register-form/register-form.component';
import { SecondaryPasswordValidator } from './directives/SecondaryPasswordValidator';
import { ExchangeRatesInfoComponent } from './components/exchange/exchange-rates-info/exchange-rates-info.component';
import { ExchangeCurrenciesComponent } from './components/exchange/exchange-currencies/exchange-currencies.component';
import { ExcludeEuroCurrencyPipe } from './pipes/exclude-euro-currency.pipe';
import { FilterExcludeValuePipe } from './pipes/filter-exclude-value.pipe';
import { FormErrorsComponent } from './components/common/form-errors/form-errors.component';
import { CurrenciesLogsPageComponent } from './components/logs/currencies-logs-page/currencies-logs-page.component';
import { NumberFixPercisionDirective } from './directives/number-input-percision.directive';
import { NumberMaximumDigitsCount } from './directives/number-input-maximum-digits.directive';
import { AuthGuard } from './routing/auth.guard';
import { AuthJsonTokenInterceptor } from './interceptors/auth-json-token.interceptor';
import { ErrorHandlerInterceptor } from './interceptors/error-handler.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { CurrencySelectComponent } from './components/exchange/currency-select/currency-select.component';
import { AppNumericRangeComponent } from './components/common/app-numeric-range/app-numeric-range.component';
import { FilterFormComponent } from './components/logs/filter-form/filter-form.component';
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginFormComponent,
    ExchangeCurrenciesPageComponent,
    CurrenciesLogsComponent,
    CurrenciesLogsPageComponent,
    LogsPaginationComponent,
    RegisterFormComponent,
    SecondaryPasswordValidator,
    ExchangeRatesInfoComponent,
    ExchangeCurrenciesComponent,
    ExcludeEuroCurrencyPipe,
    FilterExcludeValuePipe,
    FormErrorsComponent,

    NumberMaximumDigitsCount,
    NumberFixPercisionDirective,
    FilterFormComponent,
    CurrencySelectComponent,
    AppNumericRangeComponent,
  ],
  imports: [
    //
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,

    BrowserAnimationsModule,
    MatDialogModule,
  ],
  providers: [
    HttpClientModule,
    AuthJsonTokenInterceptor.getProvider(),
    ErrorHandlerInterceptor.getProvider(),
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
