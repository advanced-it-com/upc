package com.isco.upc.app.service;

import com.isco.upc.app.domain.Person;
import com.isco.upc.app.email.beans.PersonFunction;
import com.isco.upc.app.repository.FileStorageRepository;
import com.isco.upc.app.repository.PersonRepository;
import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.exceptions.UPCException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private FileStorageRepository fileStorageRepository;

    public List<Person> getAllPersons(){
    	return personRepository.findAll();
    }
    
	public Page<Person> getPagedListPerson(int page, int size, PersonFunction function, String status, String name) {
		Pageable pageableRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
		Page<Person> persons = null;
		if (StringUtils.isEmpty(name)){
			persons = personRepository.findByPersonFunctionAndStatus(function, status, pageableRequest);
		}else{
		    persons = personRepository.findByAllCriteria(function, status, name, pageableRequest);
		}
	
		
		return persons;
	}
    

    public Person getPersonByEmail(String email){
    	return personRepository.findByEmail(email);
    }
    
    public Person getPersonById(String id){
    	
    	return personRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.PERSON_ID_NOT_FOUND, String.format("Person id not found '%s'.", id)));

    	
    	
    }
    
    public Person addPerson(Person person){
       	if (personRepository.findByEmail(person.getEmail()) != null){
       		throw new UPCException(ErrorCode.PERSON_EMAIL_ALREADY_EXIST, String.format("Email already exist '%s'.", person.getEmail()));
   	    }
       	person.setId(null);
    	return personRepository.save(person); 	
    }
    
    
    public Person updatePerson(Person person){
    	String id = person.getId();
    	Person savedPerson = personRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.PERSON_ID_NOT_FOUND, String.format("Person id not found '%s'.", id)));

		return personRepository.save(person);
    	
    }


	public void delete(String id) {

		Person savedPerson = personRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.PERSON_ID_NOT_FOUND, String.format("Person id not found '%s'.", id)));

		if (savedPerson != null){
    		if (savedPerson.getPhotoFileId() != null){
    			fileStorageRepository.delete(savedPerson.getPhotoFileId());
    		}
    		
    		if (savedPerson.getCvFileId() != null){
    			fileStorageRepository.delete(savedPerson.getCvFileId());
    		}
    		
    		personRepository.delete(savedPerson);
    	}
		
	}
   
}
