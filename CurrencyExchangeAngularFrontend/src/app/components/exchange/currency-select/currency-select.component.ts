import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output,
} from '@angular/core';

@Component({
  selector: 'app-currency-select',
  templateUrl: './currency-select.component.html',
  styleUrls: ['./currency-select.component.scss'],
})
export class CurrencySelectComponent implements OnInit {
  @Input() public capture: string;
  @Input() public value: string | null;
  @Input() items: string[];
  constructor() {}

  ngOnInit(): void {}

  @Output('onChange') emitter = new EventEmitter<string>();

  onChange(data: string) {
    this.value = data;
    this.emitter.emit(data);
  }
}
