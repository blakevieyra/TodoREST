import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { Todo } from '../models/todo';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class TodoService {
  // private baseUrl = 'http://localhost:8090/';
  private url = environment.baseUrl + 'api/todos';

  constructor(private http: HttpClient, private datePipe: DatePipe, private auth: AuthService) {}

  getHttpOptions() {
  let options = {
    headers: {
      Authorization: 'Basic ' + this.auth.getCredentials(),
      'X-Requested-With': 'XMLHttpRequest',
    },
  };
  return options;
}

  index(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.url, this.getHttpOptions()).pipe(
      catchError((err: any) => {
        console.log(err);
        return throwError(
          () => new Error('TodoService.index(): error retrieving Todo: ' + err)
        );
      })
    );
  }

  show(todoId:number):Observable<Todo> {
    return this.http
      .get<Todo>(this.url + '/' + todoId, this.getHttpOptions())
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () =>
              new Error('TodoService.index(): error retrieving Todo: ' + err)
          );
        })
      );
  }

  create(todo: Todo): Observable<Todo> {
    todo.completed = false;
    todo.description = '';
    return this.http.post<Todo>(this.url, todo, this.getHttpOptions()).pipe(
      catchError((err: any) => {
        console.log(err);
        return throwError(
          () => new Error('TodoService.create(): error creating todo: ' + err)
        );
      })
    );
  }

  update(editTodo: Todo): Observable<Todo> {
    if (editTodo.completeDate) {
      editTodo.completeDate = this.datePipe.transform(Date.now(), 'shortDate'); //  7/23/23
    } else {
      editTodo.completeDate = '';
    }
    return this.http
      .put<Todo>(this.url + '/' + editTodo.id, editTodo, this.getHttpOptions())
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('TodoService.update(): error updating todo: ' + err)
          );
        })
      );
  }

  destroy(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.url}/${id}`, this.getHttpOptions())
      .pipe(
        catchError((err: any) => {
          console.log(err);
          return throwError(
            () => new Error('TodoService.delete(): error deleting todo: ' + err)
          );
        })
      );
  }
}
