package com.isco.upc.app.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isco.upc.app.domain.Person;
import com.isco.upc.app.domain.User;
import com.isco.upc.app.repository.PersonRepository;
import com.isco.upc.app.repository.UserRepository;
import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.enums.UserRole;
import com.isco.upc.common.exceptions.UPCException;
import com.isco.upc.security.beans.JWTPrincipal;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private PersonRepository personRepository;

    public List<User> getAllUsers(){
    	return userRepository.findAll();
    }
    

    public User getUserByUsername(String username){
    	return userRepository.findByUsername(username);
    	
    }
    
    public User getUserById(String id){
    	
    	return userRepository.findById(id).orElseThrow(() ->  new UPCException(ErrorCode.USER_ID_NOT_FOUND, String.format("User id not found '%s'.", id)));

    	
    	
    }
    
    public User addUser(User user){
    	if (userRepository.findByUsername(user.getUsername()) != null){
    		 throw new UPCException(ErrorCode.USER_USERNAME_ALREADY_EXIST, String.format("User name already exist '%s'.", user.getUsername()));
    	}
       	if (userRepository.findByEmail(user.getEmail()) != null){
       		throw new UPCException(ErrorCode.USER_EMAIL_ALREADY_EXIST, String.format("Email already exist '%s'.", user.getEmail()));
   	    }
       	user.setUserId(null);
       	user.setRoles(Arrays.asList(UserRole.CANDIDATE));
       /*	if (personRepository.findByEmail(user.getEmail()) == null){
           	Person p = new Person();
           	p.setEmail(user.getEmail());
           	personRepository.save(p);
       	}*/

    	return userRepository.save(user);
    	
    }
    
    
    public User updateUser(User user){
    	String id = user.getUserId();
    	User savedUser = userRepository.findById(id).orElseThrow(() ->  new UPCException(ErrorCode.USER_ID_NOT_FOUND, String.format("User id not found '%s'.", id)));

		savedUser.setUsername(user.getUsername());
    	savedUser.setRoles(user.getRoles());
    	savedUser.setPermissions(user.getPermissions());
    	return userRepository.save(savedUser);
    	
    }
    
 
    public JWTPrincipal loadUserByUsername(String username, String password)  {
      User user = this.userRepository.findByUsername(username);

      if (user == null || !user.getPassword().equals(password)) {
        throw new UPCException(ErrorCode.USER_USERNAME_NOT_FOUND, String.format("No user found with username '%s'.", username));
      }
      
      return new JWTPrincipal(
      	      user.getUserId(),
      	      user.getUsername(),
      	      user.getEmail(),
      	      user.getCompanyId(),
      	      user.getRoles(), user.getPermissions());
    }


	public void delete(String id) {

		User savedUser = userRepository.findById(id).orElseThrow(() ->  new UPCException(ErrorCode.USER_ID_NOT_FOUND, String.format("User id not found '%s'.", id)));

		userRepository.delete(savedUser);
	}
}
