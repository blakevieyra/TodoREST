package com.skilldistillery.todoapp.controllers;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilldistillery.todoapp.entities.Todo;
import com.skilldistillery.todoapp.services.TodoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin({ "*", "http://localhost/" })
@RequestMapping("api")
@RestController
public class TodoController {

	@Autowired
	private TodoService todoService;
	
//	private String Principal.getName() = "shaun";
	

	@GetMapping("todos")
	public Set<Todo> index(Principal principal, HttpServletResponse res, HttpServletRequest req) {
		return todoService.index(principal.getName());
	}

	@GetMapping(path = "todos/{tid}")
	public Todo findTodo(Principal principal, @PathVariable("tid") int tid, HttpServletResponse res, HttpServletRequest req) {
		Todo todo;
		try {
			todo = todoService.show(principal.getName(), tid);
			if (todo != null) {
				res.setStatus(200);	
			} else {
				res.setStatus(404);	
				todo = null;
			}
		} catch (Exception e) {
			res.setStatus(400);
			todo = null;
			e.printStackTrace();
		}
		return todo;
	}

	@PostMapping(path = "todos")
	public Todo createTodo(Principal principal, @RequestBody Todo todo, HttpServletResponse res, HttpServletRequest req) {
		try {
			if (todo != null) {
				res.setStatus(201);
				res.setHeader("Location", req.getRequestURL().append("/").append(todo.getId()).toString());
				return todoService.create(principal.getName(), todo);
			} else {
				res.setStatus(401);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(400);
		}
		return todo;
	}

	@PutMapping(path = "todos/{tid}")
	public Todo updateTodo(Principal principal, @PathVariable("tid") Integer tid, @RequestBody Todo todo, HttpServletResponse res,
			HttpServletRequest req) {
		Todo updated;
		try {
			updated = todoService.update(principal.getName(), tid, todo);
			if (updated == null) {
				res.setStatus(404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(400);
			updated = null;
		}
		return updated;
	}

	@DeleteMapping(path = "todos/{tid}")
	public void deleteTodo(Principal principal, @PathVariable("tid") Integer tid, HttpServletResponse res, HttpServletRequest req) {
		try {
			if (todoService.destroy(principal.getName(), tid)) {
				res.setStatus(204);
			} else {
				res.setStatus(404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(400);
		}

	}
}