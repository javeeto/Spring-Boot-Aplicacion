package com.gesdoc.grudexample.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gesdoc.grudexample.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	public Optional<User> findByUsername(String username);


}
