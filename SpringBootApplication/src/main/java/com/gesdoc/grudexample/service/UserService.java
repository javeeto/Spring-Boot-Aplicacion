package com.gesdoc.grudexample.service;

import com.gesdoc.grudexample.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
}
