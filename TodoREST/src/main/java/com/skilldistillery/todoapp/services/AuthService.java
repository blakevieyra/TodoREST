package com.skilldistillery.todoapp.services;

import com.skilldistillery.todoapp.entities.User;

public interface AuthService {
	public User register(User user);
	public User getUserByUsername(String user);
	
}
