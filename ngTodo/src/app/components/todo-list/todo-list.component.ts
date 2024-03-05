import { IncompletePipe } from './../../incomplete.pipe';
import { Component, OnInit } from '@angular/core';
import { Todo } from '../../models/todo';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TodoService } from '../../services/todo.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-todo-list',
  standalone: true,
  imports: [CommonModule, FormsModule, IncompletePipe],
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css'],
})
export class TodoListComponent implements OnInit {

  ngOnInit(): void {
    this.reload();
    this.activateRoute.paramMap.subscribe({
      next: (params) => {
        let todoIdStr = params.get('todoId');
        if (todoIdStr) {
          let todoId = parseInt(todoIdStr);
          if (isNaN(todoId)) {
            this.router.navigateByUrl('todoId');
          }
          else {
            this.getTodo(todoId);
          }
        }
      },
      error: (kaboom) => {
        console.error('Error retreiving Todo');
        console.error(kaboom);
      },
    });
  }

  constructor(
    private todoService: TodoService,
    private incompletePipe: IncompletePipe,
    private activateRoute: ActivatedRoute,
    private router: Router
  ) {}

  //loaded the method on the page once subscribed call is made
  reload(): void {
    this.todoService.index().subscribe({
      next: (todoList) => {
        this.todos = todoList;
      },
      error: (problem) => {
        console.error('TodoComponent.reload(): error loading todos: ');
        console.error(problem);
      },
    });
  }

  //initialized parameter and variables
  editTodo: Todo | null = null;
  newTodo: Todo = new Todo();
  title: string = 'Incompleted Todo Count: ';
  selected: Todo | null = null;
  todos: Todo[] = [];
  showComplete: boolean = false;

  getTodoCount() {
    return this.incompletePipe.transform(this.todos, false).length;
  }
  displayTodo(todo: Todo) {
    this.selected = todo;
  }

  displayTable() {
    this.selected = null;
  }
  setEditTodo() {
    this.editTodo = Object.assign({}, this.selected);
  }

  getTodo(todoId: number) {
    this.todoService.show(todoId).subscribe({
      next: (todo) => {
        (this.selected = todo),
        this.reload();
      },
      error: () => {
        this.router.navigateByUrl('todoNotFound')
      },
    });
  }

  addTodo(todo: Todo) {
    this.todoService.create(todo).subscribe({
      next: (createdTodo) => {
        this.newTodo = new Todo();
        this.reload();
      },
      error: () => {},
    });
  }

  updateTodo(todo: Todo, goToDetail = true) {
    console.log(todo);
    this.todoService.update(todo).subscribe({
      next: (updatedTodo) => {
        this.editTodo = null;
        if (goToDetail) {
          this.selected = updatedTodo;
        }
        this.reload();
      },
      error: (kaboom) => {
        console.error('Error updating Todo');
        console.error(kaboom);
      },
    });
  }

  deleteTodo(id: number) {
    this.todoService.destroy(id).subscribe({
      next: () => {
        this.reload();
      },
      error: () => {},
    });
  }
}
