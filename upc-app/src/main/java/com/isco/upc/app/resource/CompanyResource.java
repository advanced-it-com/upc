package com.isco.upc.app.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.isco.upc.app.domain.Company;
import com.isco.upc.app.domain.Contact;
import com.isco.upc.app.domain.Email;
import com.isco.upc.app.resource.beans.ContactCompanyBean;
import com.isco.upc.app.service.CompanyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path("/companies")
@Api(value = "/companies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {
	
	@Autowired
	CompanyService companyService;
	
	@Context
	protected SecurityContext securityContext;

//	@Secured(roles = {ADMIN} )
//	@Path("")
	@GET
	@ApiOperation(value = "List all Company", notes = "LIST ALL Companies", response = Company.class, responseContainer = "List")
	public List<Company> getAllCompanies() {
		return companyService.getAllCompanies();
	}
	
//	@Secured(roles = {ADMIN} )
	@Path("paged")
	@GET
	@ApiOperation(value = "List all companies", notes = "LIST ALL companies", response = Company.class, responseContainer = "Page")
	public Page<Company> getPagedListEmails(	@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("name") String name, @QueryParam("client") boolean isClient ) {
		return companyService.getPagedListCompanies(page, size, name, isClient);
	}
	
	@Path("contacts")
	@GET
	@ApiOperation(value = "List all Company contacts", notes = "LIST ALL Companies", response = Company.class, responseContainer = "List")
	public List<ContactCompanyBean> getAllContacts() {
		return companyService.getAllContactsCompanies();
	}

	
	@POST
	@ApiOperation(value = "add Company", notes = "USE THIS TO CREATE NEW Company")
	public void createCompany(Company company) {
		companyService.addCompany(company);
	}
	
	@POST
	@Path("bulk")
	@ApiOperation(value = "bulck Company", notes = "USE THIS TO CREATE NEW Company")
	public void bulckCompany(List<String> emails) {
		
			  List<String> insertedEmails = new ArrayList<String>();
	           Map<String, Company> companies = new HashMap<String, Company>();
	            for(String em: emails){
	            	if (insertedEmails.contains(em)){
	            		continue;
	            	}
	            	insertedEmails.add(em);
	                String [] parts = em.split("@");
	                if (parts != null && parts.length == 2){
	                    String companyName = parts [1];
	                    int lastIndex = companyName.lastIndexOf(".");
	                    if (lastIndex != -1){
	                        companyName = companyName.substring(0, lastIndex);
	                        if (companies.containsKey(companyName)){
	                        	Company c = companies.get(companyName);
	                        	Contact contact = new Contact();
	                        	contact.setEmail(em);
	                        	c.getContacts().add(contact);
	                        	companies.replace(companyName, c);
	                        }else{
	                        	Company c = new Company();
	 	                        c.setName(companyName);
	 	                        List<Contact> contacts = new ArrayList<>();
	 	                        Contact contact = new Contact();
	 	                        contact.setEmail(em);
	 	                        contacts.add(contact);
	 	                        
	 	                        c.setContacts(contacts);
	 	                        
	 	                        companies.put(companyName, c);
	                        }
	                       
	                    }
	                }
	             }
	      
	            for (Map.Entry<String, Company> entry : companies.entrySet()) {
	                companyService.addCompany(entry.getValue());
	                
	            }
	            
	            
		
	}

	
//	@Secured(roles = {ADMIN} )
	@PUT
	@ApiOperation(value = "update Company", notes = "USE THIS TO UPDATE")
	public void updateCompany(Company company) {
		companyService.updateCompany(company);
	}
	
	
	@GET
	@Path("{companyId}")
	@ApiOperation(value = "Company detail", notes = "Company", response = Company.class)
	public Company getCompanyDetail(@PathParam("companyId") String companyId) {
		return companyService.getCompanyById(companyId);
	}
	
	@DELETE
	@Path("{companyId}")
	@ApiOperation(value = "Company delete", notes = "Company", response = Void.class)
	public void deleteCompany(@PathParam("companyId") String companyId) {
		companyService.delete(companyId);
	}

}
