import {
  Validator,
  NG_VALIDATORS,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { Directive } from '@angular/core';

@Directive({
  selector: '[invalidSecondaryPasswordValidation]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: SecondaryPasswordValidator,
      multi: true,
    },
  ],
})
export class SecondaryPasswordValidator implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return this.invalidSecondaryPasswordValidationMethod(control);
  }

  invalidSecondaryPasswordValidationMethod(
    control: AbstractControl
  ): ValidationErrors | null {
    if (!this.isValid(control)) {
      return {
        invalidSecondaryPasswordValidation: true,
      };
    }
    return null;
  }

  public invalidSecondaryPasswordValidation: string;

  private isValid(control: AbstractControl): boolean {
    let parent = control.parent as AbstractControl;

    let fieldName = this.invalidSecondaryPasswordValidation
      ? this.invalidSecondaryPasswordValidation
      : 'password';
    let password = parent.get(fieldName)?.value;
    let confirmPassword = control.value;
    // if (password !== undefined)
    return confirmPassword === password;
  }
}
