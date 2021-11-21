package com.gesdoc.grudexample.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gesdoc.grudexample.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
