import { Utils } from '../utils/Utils';

export class UserCredentials {
  username: string;
  password: string;
  confirmPassword: string;

  public constructor(init?: Partial<UserCredentials>) {
    Object.assign(this, init);
  }

  public toFormData() {
    return Utils.toFormData(this);
  }
}

export class CurrencyExchangeRate {
  currency: string;
  rate: number;
  inExchangeListHidden?: boolean;

  public constructor(init?: Partial<CurrencyExchangeRate>) {
    Object.assign(this, init);
  }
}

export class CurrencyExchangeTimeAndRates {
  public static emptyInstance = new CurrencyExchangeTimeAndRates({
    notAvailable: true,
  });

  exchangeRates: CurrencyExchangeRate[];
  time: Date;
  notAvailable = false;

  public constructor(init?: Partial<CurrencyExchangeTimeAndRates>) {
    Object.assign(this, init);
  }
}

export class ExchangeCurrencyLogEvent {
  currencyAmountSold: number;
  currencySold: CurrencyExchangeRate;
  currencyPurchase: CurrencyExchangeRate;
  currencyAmountReceived: number;
  userName: string;
  createdAt: number;

  public constructor(init?: Partial<ExchangeCurrencyLogEvent>) {
    Object.assign(this, init);
  }
}
export class PageRequestData {
  pageNumber: number;
  pageSize: number;
  offset: number;
}

export class PageRequest {
  offset: number;
  pageSize: number;
  pageNumber: number;
}
export class ExchangeCurrencyLogEventsData {
  content: ExchangeCurrencyLogEvent[];
  pageable: PageRequest;

  totalPages: number;
  totalElements: number;
  numberOfElements: number;

  public constructor(init?: Partial<ExchangeCurrencyLogEventsData>) {
    Object.assign(this, init);
  }
}

export class ExchangeCurrencyLogEvents {
  public static emptyInstance = new ExchangeCurrencyLogEvents();
  data: ExchangeCurrencyLogEventsData;
  pageRequestData: PageRequestData;

  public constructor(init?: Partial<ExchangeCurrencyLogEvents>) {
    Object.assign(this, init);
  }
}

export class CurrencyExchangeRequest {
  currencySold: string;
  currencyPurchase: string;

  currencyAmountSold: number;

  public toFormData() {
    return Utils.toFormData(this);
  }

  public constructor(init?: Partial<CurrencyExchangeRequest>) {
    Object.assign(this, init);
  }

  public reset() {
    this.currencySold = '';
    this.currencyPurchase = '';
    this.currencyAmountSold = 0;
  }

  public isFieldsFilled() {
    return (
      this.currencySold && this.currencyPurchase && this.currencyAmountSold
    );
  }
}
export class ErrorEventData {
  errorCapture: string;
  message: string;
  status?: number;

  public constructor(errorCapture: string, message: string, status?: number) {
    this.errorCapture = errorCapture;
    this.message = message;
    this.status = status;
  }
}
export class ExchangeCurrencyLogEventQueryData {
  public constructor(init?: Partial<ExchangeCurrencyLogEventQueryData>) {
    Object.assign(this, init);
  }
  reset() {
    this.currencySold = null;
    this.currencyPurchase = null;
    this.currencyAmountSoldMin = null;
    this.currencyAmountSoldMax = null;
    this.currencyAmountReceivedMin = null;
    this.currencyAmountReceivedMax = null;
    this.createdAtStart = null;
    this.createdAtEnd = null;
    this.userNameLike = null;
    this.orderBy = null;
  }

  currencySold: string | null;
  currencyPurchase: string | null;
  currencyAmountSoldMin: number | null;
  currencyAmountSoldMax: number | null;
  currencyAmountReceivedMin: number | null;
  currencyAmountReceivedMax: number | null;
  createdAtStart: Date | null;
  createdAtEnd: Date | null;
  userNameLike: string | null;
  orderBy: string | null;
}
