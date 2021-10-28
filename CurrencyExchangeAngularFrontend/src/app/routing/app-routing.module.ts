import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from '../components/login/login-form/login-form.component';
import { ExchangeCurrenciesPageComponent } from '../components/exchange/exchange-currenicies-page/exchange-currencies-page.component';
import { CurrenciesLogsComponent } from '../components/logs/currencies-logs/currencies-logs.component';
import { RegisterFormComponent } from '../components/login/register-form/register-form.component';
import { CurrenciesLogsPageComponent } from '../components/logs/currencies-logs-page/currencies-logs-page.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
  //
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'login', component: LoginFormComponent },
  { path: 'registration', component: RegisterFormComponent },
  {
    path: 'exchange',
    component: ExchangeCurrenciesPageComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'logs',
    component: CurrenciesLogsPageComponent,
    canActivate: [AuthGuard],
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard],
})
export class AppRoutingModule {}
