package com.gesdoc.grudexample.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gesdoc.grudexample.dto.ChangePasswordForm;
import com.gesdoc.grudexample.entity.User;
import com.gesdoc.grudexample.exception.UsernameOrIdNotFound;
import com.gesdoc.grudexample.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository usersRepo;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;	
	
	@Override
	public Iterable<User> getAllUsers() {
		// TODO Auto-generated method stub
		return usersRepo.findAll();
	}

	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = usersRepo.findByUsername(user.getUsername());
		if (userFound.isPresent()) {
			throw new Exception("Nombre de usuario no disponible");
		}
		return true;
	}

	private boolean checkEmailAvailable(User user) throws Exception {
		Optional<User> userFound = usersRepo.findByEmail(user.getEmail());
		if (userFound.isPresent()) {
			throw new Exception("Email ya registrado");
		}
		return true;
	}

	private boolean checkPasswordValid(User user) throws Exception {
		if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new Exception("Confirm Password es obligatorio");
		}

		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Clave y confirmación no son iguales");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		// TODO Auto-generated method stub
		if (this.checkPasswordValid(user) && this.checkUsernameAvailable(user) && this.checkEmailAvailable(user)) {
			
			String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user = usersRepo.save(user);

		}

		return user;
	}

	@Override
	public User getUserById(Long id) throws UsernameOrIdNotFound {
		// TODO Auto-generated method stub
		User user = usersRepo.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("Usuario no existe"));
		return user;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public User updateUser(User fromUser) throws Exception {
		User toUser = this.getUserById(fromUser.getId());
		this.mapUser(fromUser, toUser);
		return usersRepo.save(toUser);
	}

	/**
	 * Map everything but the password.
	 * 
	 * @param from
	 * @param to
	 */
	protected void mapUser(User from, User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws UsernameOrIdNotFound {
		User userDel = getUserById(id);

		usersRepo.delete(userDel);

	}

	public boolean isLoggedUserADMIN() {
		return loggedUserHasRole("ROLE_ADMIN");
	}

	public boolean loggedUserHasRole(String role) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null;
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			roles = loggedUser.getAuthorities().stream().filter(x -> role.equals(x.getAuthority())).findFirst()
					.orElse(null); // loggedUser = null;
		}
		return roles != null ? true : false;
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = usersRepo.findById(form.getId())
				.orElseThrow(() -> new Exception("UsernotFound in ChangePassword -" + this.getClass().getName()));

		if (!isLoggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception("Clave actual invalido");
		}

		if (user.getPassword().equals(form.getNewPassword())) {
			throw new Exception("Clave nueva debe ser diferente a una registrada");
		}

		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("Claves no coinciden");
		}

		String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePassword);
		
		return usersRepo.save(user);
	}

}
