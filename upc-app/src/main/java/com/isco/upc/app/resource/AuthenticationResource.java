package com.isco.upc.app.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.isco.upc.app.resource.beans.AuthenticationRequest;
import com.isco.upc.app.service.UserService;
import com.isco.upc.security.beans.JWTPrincipal;
import com.isco.upc.security.utils.TokenUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/auth")
public class AuthenticationResource {
	
	  @Autowired
	  private UserService userService;


	  @POST
	  @Produces("application/json")
	  @Consumes("application/json")
	  @ApiOperation(value = "Login", notes = "LOGIN", response = AuthenticationRequest.class)
	  public String authenticationRequest( AuthenticationRequest authenticationRequest)  {
	    // Perform the authentication
	    JWTPrincipal userDetails = userService.loadUserByUsername(authenticationRequest.getUsername(), authenticationRequest.getPassword() );
	    //Genrate token
	    return TokenUtils.getInstance().generateToken(userDetails);
	  }
	  
	



}
