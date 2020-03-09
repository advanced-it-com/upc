package com.isco.upc.app.resource;

import static com.isco.upc.common.enums.UserRole.ADMIN;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.isco.upc.app.domain.Person;
import com.isco.upc.app.domain.User;
import com.isco.upc.app.service.UserService;
import com.isco.upc.security.annotations.Secured;
import com.isco.upc.security.beans.JWTPrincipal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path("/users")
@Api(value = "/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	@Autowired
	UserService userService;
	
	@Context
	protected SecurityContext securityContext;

//	@Secured(roles = {ADMIN} )
//	@Path("")
	@GET
	@ApiOperation(value = "List all user", notes = "LIST ALL STORES", response = User.class, responseContainer = "List")
	public List<User> getAllUsers() {
		// JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		return userService.getAllUsers();
	}

	@Path("register")
	@POST
	@ApiOperation(value = "register user", notes = "USE THIS TO CREATE NEW USER")
	public void createUser(User user) {
		userService.addUser(user);
	}

	
//	@Secured(roles = {ADMIN} )
	@PUT
	@ApiOperation(value = "update user", notes = "USE THIS TO UPDATE")
	public void updateUser(User user) {
		userService.updateUser(user);
	}
	
	
	@GET
	@Path("{userId}")
	@ApiOperation(value = "User detail", notes = "User", response = User.class)
	public User getUserDetail(@PathParam("userId") String userId) {
		// JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		return userService.getUserById(userId);
	}
	
	@Secured
	@GET
	@Path("connected")
	@ApiOperation(value = "User detail", notes = "User", response = User.class)
	public User getPersonDetaiByToken() {
		 JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		return userService.getUserByUsername(p.getName());
	}
	
	@DELETE
	@Path("{userId}")
	@ApiOperation(value = "User delete", notes = "User", response = Void.class)
	public void deleteUser(@PathParam("userId") String userId) {
		// JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		 userService.delete(userId);
	}

}
