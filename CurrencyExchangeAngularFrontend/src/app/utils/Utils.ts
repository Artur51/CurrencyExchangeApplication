import { HttpParams } from '@angular/common/http';

export class Utils {
  /**
   *
   * @param state Validate if provided input field is in valid error state.
   * @returns
   */
  public static isFieldValid(state: any) {
    return !state.dirty || !state.invalid || state.pending;
  }

  public static isFormValid(state: any) {
    return !state.invalid || state.pending;
  }

  public static toFormData(item: any): FormData {
    var formData = new FormData();
    for (var key in item) {
      const data = item[key] as unknown as string;
      formData.append(key, data);
    }
    return formData;
  }

  public static toHttpParams(item: any): HttpParams {
    const params = new HttpParams();
    for (var key in item) {
      const data = item[key] as unknown as string;
      params.set(key, data);
    }
    return params;
  }

  public static removeFromStart(items: any[], keepCount: number) {
    while (items.length > keepCount) {
      items.shift();
    }
  }

  /**
   * Collect error messages for given input field.
   * @param state angular form field reference variable to get error state from.
   * @param thingName? optional alias name for given form field to use in error message.
   * @returns
   */
  public static getValidationMessages(state: any, thingName?: string) {
    let thing: string = state.path || thingName;
    let messages: string[] = [];
    if (state.errors) {
      for (let errorName in state.errors) {
        switch (errorName) {
          case 'invalidSecondaryPasswordValidation':
            messages.push(`Entered passwords must be equals.`);
            break;
          case 'required':
            messages.push(`You must enter a ${thing}.`);
            break;
          case 'minlength':
            messages.push(
              `A ${thing} must be at least ${state.errors['minlength'].requiredLength} characters.`
            );
            break;
          case 'maxlength':
            messages.push(
              `A ${thing} must be at not longer then ${state.errors['maxlength'].requiredLength} characters.`
            );
            break;
          case 'pattern':
            messages.push(`The ${thing} contains illegal characters`);
            break;
        }
      }
    }
    return messages;
  }
}

declare global {
  interface Object {
    json(): string;
    j(): string;
  }
}
Object.prototype.json = function () {
  return JSON.stringify(this);
};
Object.prototype.j = function () {
  return JSON.stringify(this);
};
