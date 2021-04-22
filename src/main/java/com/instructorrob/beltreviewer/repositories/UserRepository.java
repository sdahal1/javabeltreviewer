package com.instructorrob.beltreviewer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.instructorrob.beltreviewer.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByEmail(String email);

}