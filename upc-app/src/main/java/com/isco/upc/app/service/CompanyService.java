package com.isco.upc.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.isco.upc.app.domain.Company;
import com.isco.upc.app.repository.CompanyRepository;
import com.isco.upc.app.repository.FileStorageRepository;
import com.isco.upc.app.resource.beans.ContactCompanyBean;
import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.exceptions.UPCException;


@Service
public class CompanyService {
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private FileStorageRepository fileStorageRepository;

    public List<Company> getAllCompanies(){
    	return companyRepository.findAll();
    }
    

    public Company getCompanyByEmail(String email){
    	return companyRepository.findByEmail(email);
    	
    }
    
    public Company getCompanyById(String id){
    	
    	Company company = companyRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));

    	return company;
    	
    	
    }
    
    public Company addCompany(Company company){
       	if (companyRepository.findByName(company.getName()) != null){
       		throw new UPCException(ErrorCode.COMPANY_NAME_ALREADY_EXIST, String.format("Name already exist '%s'.", company.getEmail()));
   	    }
       	company.setId(null);
       	company.setCreation(new Date());
    	return companyRepository.save(company); 	
    }
    
    
    public Company updateCompany(Company company){
    	String id = company.getId();
    	Company savedCompany = companyRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));

		company.setCreation(savedCompany.getCreation());
    	return companyRepository.save(company); 
    }


	public void delete(String id) {
		Company savedCompany =  companyRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));

		if (savedCompany != null){
    		if (savedCompany.getLogoId() != null){
    			fileStorageRepository.delete(savedCompany.getLogoId());
    		}
    	
    		companyRepository.delete(savedCompany);
    	}
		
	}


	public List<ContactCompanyBean> getAllContactsCompanies() {
		List<ContactCompanyBean> contactCompanies = new ArrayList<>();
		List<Company> companies = companyRepository.findAll();
		
		companies.stream()
		         .filter(c-> c.getContacts() != null)
				 .map(c ->  c.getContacts().stream().filter(t-> ( t.getEmail() != null &  !Boolean.TRUE.equals(t.getEmailDisabled()))).map(t -> {
			
						 ContactCompanyBean cc = new ContactCompanyBean();
						 cc.setCompanyId(c.getId());
						 cc.setCompanyName(c.getName());
						 cc.setLogoId(c.getLogoId());
						 cc.setFirstName(t.getFirstName());
						 cc.setLastName(t.getLastName());
						 cc.setEmail(t.getEmail());
						 return cc;
		
				 }).collect(Collectors.toList()))
				.forEach(contactCompanies::addAll);
		return contactCompanies;
		
	}


	public Page<Company> getPagedListCompanies(int page, int size, String name, Boolean onlyClient) {
		Boolean client = onlyClient;

		Pageable pageableRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creation"));
		Page<Company> companies = null;
		if ( Boolean.FALSE.equals(client)){
			client = null;
		}
		if ( (name == null || name.isEmpty()) && client == null){
			companies = companyRepository.findAll( pageableRequest);
		}else{
			companies = companyRepository.findByAllCriteria(name, client, pageableRequest);
		}
		/*if (name != null && !name.isEmpty() && onlyClient != null && onlyClient.equals(Boolean.TRUE)){
			companies = companyRepository.findByNameLikeAndClient(name, onlyClient, pageableRequest);
		}else if (name != null && !name.isEmpty()){
			companies = companyRepository.findByNameLike(name, pageableRequest);
		}else if (onlyClient != null && onlyClient.equals(Boolean.TRUE)){
			companies = companyRepository.findByClient(onlyClient, pageableRequest);
		}else{
			companies = companyRepository.findAll( pageableRequest);
		}*/
		return companies;
	}
   
}
