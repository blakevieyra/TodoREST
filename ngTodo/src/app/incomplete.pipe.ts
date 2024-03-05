import { Pipe, PipeTransform } from '@angular/core';
import { Todo } from './models/todo';

@Pipe({
  name: 'incomplete',
  standalone: true,
})

export class IncompletePipe implements PipeTransform {
  transform(todos: Todo[], showComplete: boolean): Todo[] {
    const incomplete: Todo[] = [];
    for (let todo of todos) {
      if (showComplete) {
        return todos;
      }
      if (!todo.completed) {
        incomplete.push(todo);
      }
    }
    return incomplete;
  }
}
