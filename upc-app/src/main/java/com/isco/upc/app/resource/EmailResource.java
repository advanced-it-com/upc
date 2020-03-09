package com.isco.upc.app.resource;

import java.io.IOException;
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

import com.isco.upc.app.domain.Email;
import com.isco.upc.app.email.beans.EmailStatus;
import com.isco.upc.app.domain.Email;
import com.isco.upc.app.service.EmailService;
import com.isco.upc.security.annotations.Secured;
import com.isco.upc.security.beans.JWTPrincipal;

import freemarker.template.TemplateException;

import com.isco.upc.app.service.EmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path("/emails")
@Api(value = "/emails")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmailResource {
	
	@Autowired
	EmailService emailService;
	
	@Context
	protected SecurityContext securityContext;

//	@Secured(roles = {ADMIN} )
//	@Path("")
	@GET
	@ApiOperation(value = "List all Email", notes = "LIST ALL Email", response = Email.class, responseContainer = "List")
	public List<Email> getAllEmails() {
		return emailService.getAllEmails();
	}
	
//	@Secured(roles = {ADMIN} )
	@Path("paged")
	@GET
	@ApiOperation(value = "List all Email", notes = "LIST ALL Email", response = Email.class, responseContainer = "List")
	public Page<Email> getPagedListEmails(	@QueryParam("page") int page, @QueryParam("size") int size) {
		return emailService.getPagedListEmails(page, size);
	}
	
	@Secured
	@GET
	@Path("/template/{personId}")
	@ApiOperation(value = "Generate Email commercial", notes = "string", response = String.class)
	public String generateEmailContentTemplate(@PathParam("personId") String personId) throws IOException, TemplateException {
		 JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		return emailService.generateEmailContentTemplate(personId, p.getUserId());
	}

	
	@POST
	@ApiOperation(value = "add Email", notes = "USE THIS TO CREATE NEW Email")
	public void createEmail(Email Email) {
		emailService.addEmail(Email);
	}

	
//	@Secured(roles = {ADMIN} )
	@PUT
	@ApiOperation(value = "update Email", notes = "USE THIS TO UPDATE")
	public void updateEmail(Email Email) {
		emailService.updateEmail(Email);
	}
	
	@Path("status/{emailId}")
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "update Email status", notes = "USE THIS TO UPDATE")
	public void updateEmailStats(@PathParam("emailId") String emailId, String status) {
		emailService.updateEmailStatus(emailId, EmailStatus.valueOf(status));
	}
	
	@Secured
	@Path("send")
	@POST
	@ApiOperation(value = "send Email", notes = "USE THIS TO send")
	public void sendEmail(Email Email) {
		 JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		emailService.sendEmail(Email, p.getUserId(), false);
	}
	
	
	@Secured
	@Path("sendmass")
	@POST
	@ApiOperation(value = "send Email Mass", notes = "USE THIS TO send")
	public void sendEmailMass(Email Email) {
		 JWTPrincipal p = (JWTPrincipal) securityContext.getUserPrincipal();
		emailService.sendEmail(Email, p.getUserId(), true);
	}
	
	
	@GET
	@Path("{emailId}")
	@ApiOperation(value = "Email detail", notes = "email", response = Email.class)
	public Email getEmailDetail(@PathParam("emailId") String emailId) {
		return emailService.getEmailById(emailId);
	}
	
	@DELETE
	@Path("{emailId}")
	@ApiOperation(value = "Email delete", notes = "email", response = Void.class)
	public void deleteEmail(@PathParam("emailId") String emailId) {
		emailService.delete(emailId);
	}

}
