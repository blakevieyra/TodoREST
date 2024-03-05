package com.skilldistillery.todoapp.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.skilldistillery.todoapp.entities.Todo;

@Service
public interface TodoRepository extends JpaRepository<Todo, Integer> {

	Set<Todo>findByUser_Username(String username);
	Todo findByUser_UsernameAndId(String username,int id);
	boolean existsByUser_UsernameAndId(String username, int tid);
}
