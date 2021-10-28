import {
  Directive,
  ElementRef,
  HostListener,
  Input,
  SimpleChanges,
} from '@angular/core';

@Directive({
  selector: '[NumberMaximumDigitsCount]',
})
export class NumberMaximumDigitsCount {
  @Input('NumberMaximumDigitsCount') maximumLength: number = 13;

  @HostListener('keyup', ['$event'])
  public onKeyup(event: KeyboardEvent): void {
    let e = <KeyboardEvent>event;
    if (this.isNumericKey(e)) {
      let value = (event.target as HTMLInputElement).value;
      if (value.length > this.maximumLength)
        value = value.substring(0, this.maximumLength);
      (event.target as HTMLInputElement).value = value;
    }
  }

  // test directive should change value to match percision parametr
  // test directive should change value only when numeric key button pressed.
  private isNumericKey(e: KeyboardEvent) {
    return (
      (e.keyCode >= 48 && e.keyCode <= 57) ||
      (e.keyCode >= 96 && e.keyCode <= 105)
    );
  }
}
