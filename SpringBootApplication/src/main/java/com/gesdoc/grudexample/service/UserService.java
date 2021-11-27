package com.gesdoc.grudexample.service;

import com.gesdoc.grudexample.dto.ChangePasswordForm;
import com.gesdoc.grudexample.entity.User;
import com.gesdoc.grudexample.exception.UsernameOrIdNotFound;

public interface UserService {

	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
	
	public User getUserById(Long id) throws UsernameOrIdNotFound;
	
	public User updateUser(User fromUser) throws Exception;
	
	public void deleteUser(Long id) throws UsernameOrIdNotFound;
	
	public User changePassword(ChangePasswordForm form) throws Exception;
}
