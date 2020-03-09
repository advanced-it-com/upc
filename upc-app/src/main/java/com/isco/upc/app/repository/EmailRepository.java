package com.isco.upc.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.isco.upc.app.domain.Email;
import com.isco.upc.app.email.beans.EmailStatus;


public interface EmailRepository extends MongoRepository<Email, String> {

	Email findByName(String name);
	
	
	List<Email> findByStatus(EmailStatus status);
	
	

}
