package com.isco.upc.app.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.isco.upc.app.domain.Person;
import com.isco.upc.app.domain.User;
import com.isco.upc.app.email.beans.PersonFunction;
import com.isco.upc.app.service.PersonService;
import com.isco.upc.security.annotations.Secured;
import com.isco.upc.security.beans.JWTPrincipal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path("/persons")
@Api(value = "/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
	
	@Autowired
	PersonService personService;
	
	@Context
	protected SecurityContext securityContext;

//	@Secured(roles = {ADMIN} )
//	@Path("/all")
	@GET
	@ApiOperation(value = "List all persons", notes = "LIST ALL PERSONS", response = User.class, responseContainer = "List")
	public List<Person> getAllPerson() {
		return personService.getAllPersons();
	}
	
	@Path("/paged")
	@GET
	@ApiOperation(value = "List all persons", notes = "LIST ALL PERSONS", response = User.class, responseContainer = "List")
	public Page<Person> getPagedPerson(	@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("function")PersonFunction personFunction,  @QueryParam("status")String status, @QueryParam("name")String name) {
		return personService.getPagedListPerson(page, size, personFunction, status, name);
	}

	//@Path("/")
	@POST
	@ApiOperation(value = "add persons", notes = "USE THIS TO CREATE NEW PERSON")
	public String createPerson(Person person) {
		return personService.addPerson(person).getId();
	}

	
//	@Secured(roles = {ADMIN} )
	@PUT
	@ApiOperation(value = "update user", notes = "USE THIS TO UPDATE")
	public void updatePerson(Person person) {
		personService.updatePerson(person);
	}

	@GET
	@Path("{personId}")
	@ApiOperation(value = "Person detail", notes = "Person", response = Person.class)
	public Person getPersonDetail(@PathParam("personId") String id) {
		// JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		return personService.getPersonById(id);
	}
	
	@Secured
	@GET
	@Path("connected")
	@ApiOperation(value = "Person detail", notes = "Person", response = Person.class)
	public Person getPersonDetaiByToken() {
		 JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		return personService.getPersonByEmail(p.getEmail());
	}
	
	
	@DELETE
	@Path("{personId}")
	@ApiOperation(value = "Person delete", notes = "Person", response = Void.class)
	public void deletePerson(@PathParam("personId") String id) {
		// JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		 personService.delete(id);
	}
}
