import {
  Directive,
  ElementRef,
  HostListener,
  Input,
  SimpleChanges,
} from '@angular/core';

@Directive({
  selector: '[NumberInpupPercision]',
})
export class NumberFixPercisionDirective {
  @Input('NumberInpupPercision') percision: number;

  @HostListener('keyup', ['$event'])
  public onKeyup(event: KeyboardEvent): void {
    let e = <KeyboardEvent>event;
    if (this.isNumericKey(e)) {
      let value: number = +(event.target as HTMLInputElement).value;
      let fixed = value.toFixed(this.percision);
      (event.target as HTMLInputElement).value = fixed;
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
