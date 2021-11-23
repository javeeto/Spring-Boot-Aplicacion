package com.gesdoc.grudexample.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gesdoc.grudexample.entity.User;
import com.gesdoc.grudexample.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository usersRepo;
	
	@Override
	public Iterable<User> getAllUsers() {
		// TODO Auto-generated method stub
		return usersRepo.findAll();
	}
	
	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = usersRepo.findByUsername(user.getUsername());
		if(userFound.isPresent()) {
			throw new Exception("Nombre de usuario no disponible");
		}
		return true;
	}
	
	
	private boolean checkPasswordValid(User user) throws Exception {
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Clave y confirmación no son iguales");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		// TODO Auto-generated method stub
		if(this.checkPasswordValid(user)&&
				this.checkUsernameAvailable(user)) {
			user = usersRepo.save(user);
			
		}
		
		return user;
	}
	
	

}
