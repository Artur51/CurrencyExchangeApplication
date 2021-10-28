import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-numeric-range',
  templateUrl: './app-numeric-range.component.html',
  styleUrls: ['./app-numeric-range.component.scss'],
})
export class AppNumericRangeComponent implements OnInit {
  @Input() public max: any;
  @Input() public min: any;
  @Input() public label: string;

  @Output('onChangeMin') emitterMin = new EventEmitter<string>();
  @Output('onChangeMax') emitterMax = new EventEmitter<string>();

  form;
  constructor() {
    this.form = new FormGroup({
      min: new FormControl(''),
      max: new FormControl(''),
    });
  }

  ngOnInit(): void {}

  onChangeMin($event: any) {
    const value = $event.target.value;
    this.emitterMin.emit(value);
  }
  onChangeMax($event: any) {
    const value = $event.target.value;
    this.emitterMax.emit(value);
  }
}
