import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { Utils } from 'src/app/utils/Utils';

@Component({
  selector: 'app-form-errors',
  templateUrl: './form-errors.component.html',
  styleUrls: ['./form-errors.component.scss'],
})
export class FormErrorsComponent implements OnInit {
  @Input() public component: any;
  constructor() {}

  ngOnInit(): void {}

  public isValid(state: any) {
    return Utils.isFieldValid(state);
  }

  public getValidationMessages(state: any, thingName?: string) {
    return Utils.getValidationMessages(state, thingName);
  }
}
