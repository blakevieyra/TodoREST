package com.skilldistillery.todoapp.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.todoapp.entities.Todo;
import com.skilldistillery.todoapp.entities.User;
import com.skilldistillery.todoapp.repositories.TodoRepository;
import com.skilldistillery.todoapp.repositories.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public Set<Todo> index(String username) {
		return todoRepo.findByUser_Username(username);
	}

	@Override
	public Todo show(String username, int tid) {
			return todoRepo.findByUser_UsernameAndId(username, tid);
	}
	
	@Override
	public Todo create(String username, Todo todo) {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			todo.setUser(user);
			return todoRepo.saveAndFlush(todo);
		}
		return null;
	}

	@Override
	public Todo update(String username, int tid, Todo todo) {
		Todo foundTodo = todoRepo.findByUser_UsernameAndId(username, tid);
		if (foundTodo != null) {
			foundTodo.setTask(todo.getTask());
			foundTodo.setDescription(todo.getDescription());
			foundTodo.setCompleted(todo.isCompleted());
			foundTodo.setDueDate(todo.getDueDate());
			foundTodo.setCompletedDate(todo.getCompletedDate());
			return todoRepo.saveAndFlush(foundTodo);
		}
		return null;
	}

	@Override
	public boolean destroy(String username, int tid) {
		boolean isDeleted = false;
		if (todoRepo.existsByUser_UsernameAndId(username, tid)) {
			todoRepo.deleteById(tid);
			isDeleted = true;
		}
		return isDeleted;
	}
}
