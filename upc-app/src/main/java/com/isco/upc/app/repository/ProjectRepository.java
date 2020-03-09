package com.isco.upc.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.isco.upc.app.domain.Project;


public interface ProjectRepository extends MongoRepository<Project, String> {
	


}
