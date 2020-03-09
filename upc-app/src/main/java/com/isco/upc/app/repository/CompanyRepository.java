package com.isco.upc.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.isco.upc.app.domain.Company;
import com.isco.upc.app.domain.Person;
import com.isco.upc.app.email.beans.PersonFunction;


public interface CompanyRepository extends MongoRepository<Company, String> {

	Company findByEmail(String email);
	
	Company findByName(String name);
	
	 Page<Company> findByNameLikeOrClient(String name, Boolean client, Pageable pageable);
	
	 Page<Company> findByNameLikeAndClient(String name, Boolean client, Pageable pageable);
	 
	 Page<Company> findAll(Pageable pageable);
	 
	 Page<Company> findByNameLike(String name,  Pageable pageable);
	 
	 Page<Company> findByClient(Boolean client,  Pageable pageable);
	 
	 
	 @Query("{$and: [{$or : [ { $where: '?0 == null' } , { name :  { $regex: ?0, $options: 'i' } }, { contacts.email :  { $regex: ?0, $options: 'i' } } ]},  {$or : [ { $where: '?1 == null' } , { client : ?1 }]  } ] }")
	 Page<Company> findByAllCriteria(String name,  Boolean client, Pageable pageable);

		


}
