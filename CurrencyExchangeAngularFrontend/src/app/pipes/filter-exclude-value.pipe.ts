import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterExcludeValue',
})
export class FilterExcludeValuePipe implements PipeTransform {
  transform(value: string[], arg: string, isEnabled: boolean = true): string[] {
    if (isEnabled && arg !== '') {
      return value.filter((a) => arg !== a);
    }
    return value;
  }
}
