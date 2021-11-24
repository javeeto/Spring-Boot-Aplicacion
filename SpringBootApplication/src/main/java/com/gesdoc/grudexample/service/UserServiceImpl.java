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
	
	private boolean checkEmailAvailable(User user) throws Exception {
		Optional<User> userFound = usersRepo.findByEmail(user.getEmail());
		if(userFound.isPresent()) {
			throw new Exception("Email ya registrado");
		}
		return true;
	}
	
	
	private boolean checkPasswordValid(User user) throws Exception {
		if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new Exception("Confirm Password es obligatorio");
		}
		
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Clave y confirmación no son iguales");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		// TODO Auto-generated method stub
		if(this.checkPasswordValid(user)&&
				this.checkUsernameAvailable(user)&&
				this.checkEmailAvailable(user)) {
			user = usersRepo.save(user);
			
		}
		
		return user;
	}

	@Override
	public User getUserById(Long id) throws Exception {
		// TODO Auto-generated method stub
		User user =  usersRepo.findById(id).orElseThrow(() -> new Exception("Usuario no existe"));
		return user;
	}

	@Override
	public User updateUser(User fromUser) throws Exception {
		User toUser = this.getUserById(fromUser.getId());
		this.mapUser(fromUser, toUser);
		return usersRepo.save(toUser);
	}
	
	/**
	 * Map everything but the password.
	 * @param from
	 * @param to
	 */
	protected void mapUser(User from,User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}
	

}
