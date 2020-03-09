package com.isco.upc.app.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;

import com.isco.upc.app.domain.Company;
import com.isco.upc.app.repository.PersonRepositoryCustom;

public class PersonRepositoryImpl implements PersonRepositoryCustom {

	  
    @Autowired private MongoOperations operations;

  
	@Override
	public Page<Company> findCustomAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
