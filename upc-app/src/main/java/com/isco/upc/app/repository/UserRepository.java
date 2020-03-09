package com.isco.upc.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.isco.upc.app.domain.User;


public interface UserRepository extends MongoRepository<User, String> {
	
	public User findByUsername(String username);

	public User findByEmail(String email);

}
