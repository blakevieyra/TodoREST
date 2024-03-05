package com.skilldistillery.todoapptest.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skilldistillery.todoapp.entities.Todo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

class TestTodo {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Todo todo;

	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("TodoJPA");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emf.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		em = emf.createEntityManager();
		todo = em.find(Todo.class, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		em.close();
		todo = null;
	}

	@Test
	void test_todo_Has_task() {
		assertNotNull(todo);
		assertEquals("Go round Mum's", todo.getTask());
		
	}
	
	@Test
	void test_todo_Has_User() {
		assertNotNull(todo);
		assertEquals("shaun", todo.getUser().getUsername());	
	}
}
