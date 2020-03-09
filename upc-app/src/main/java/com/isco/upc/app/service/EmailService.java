package com.isco.upc.app.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.isco.upc.app.domain.Company;
import com.isco.upc.app.domain.Email;
import com.isco.upc.app.domain.Person;
import com.isco.upc.app.domain.User;
import com.isco.upc.app.email.beans.EmailBean;
import com.isco.upc.app.email.beans.EmailStatus;
import com.isco.upc.app.email.service.EmailSenderService;
import com.isco.upc.app.repository.CompanyRepository;
import com.isco.upc.app.repository.EmailRepository;
import com.isco.upc.app.repository.PersonRepository;
import com.isco.upc.app.repository.UserRepository;
import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.exceptions.UPCException;
import com.mongodb.gridfs.GridFSDBFile;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@Service
public class EmailService {
	   /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    

    
	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	private FileService fileService;
	
    @Value("${mail.from}")
    private String from;
	
	
    @Autowired
    private Configuration freemarkerConfig;

    public List<Email> getAllEmails(){
    	return emailRepository.findAll();
    }
    
    public Page<Email>  getPagedListEmails(int page, int size){
    	Pageable pageableRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creation"));
    	return emailRepository.findAll(pageableRequest);
    }
    
    public Email getEmailById(String id){
    	Email email = emailRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));

    	return email;
    }
    
    public Email addEmail(Email email){
       	if (emailRepository.findByName(email.getName()) != null){
       		throw new UPCException(ErrorCode.EMAIL_NAME_ALREADY_EXIST, String.format("Email template name already exist '%s'.", email.getName()));
   	    }
       	email.setId(null);
    	email.setFrom(from);
    	email.setStatus(EmailStatus.SAVED);
    	return emailRepository.save(email); 	
    }
    
    public Email updateEmail(Email email){
    	String id = email.getId();
    	Email savedEmail = emailRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));

		email.setFrom(from);
    	return emailRepository.save(email); 	
    }
    
    public Email updateEmailStatus(String emailId, EmailStatus status){
		String id = emailId;
		Email savedEmail = emailRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));

		savedEmail.setStatus(status);
    	return emailRepository.save(savedEmail); 	
    }

    
    public String generateEmailContentTemplate(String personId, String senderUserId) throws IOException, TemplateException{
    	
    	Person person = personRepository.findById(personId).orElseThrow(() -> new UPCException(ErrorCode.PERSON_ID_NOT_FOUND, String.format("Person id not found '%s'.", personId)));


		User user = userRepository.findById(senderUserId).orElseThrow(()-> new UPCException(ErrorCode.USER_ID_NOT_FOUND, String.format("User id not found '%s'.", senderUserId)));

    	Person sender = personRepository.findByEmail(user.getEmail());
    	if (sender == null){
       		throw new UPCException(ErrorCode.PERSON_EMAIL_NOT_FOUND, String.format("Email Person id not found '%s'.", user.getEmail()));  		  
    	}
    	
	  	 Map<String, Object> model = new HashMap<>();
	  	 model.put("person", person);
	  	 model.put("sender", sender);
	  	 
	  	if (user.getCompanyId() != null){
	  		Company company = companyRepository.findById(user.getCompanyId()).orElse(null);
	    	if (company != null){
	    		 model.put("companyName", company.getName());
	       		//throw new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", user.getCompanyId()));  		  
	    	}
	  	}

	  	
	      // set loading location to src/main/resources
	      // You may want to use a subfolder such as /templates here
	      freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/email-templates");
	      
	      Template t = freemarkerConfig.getTemplate("commercial-consultant.ftl");
	      String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
    	

      
    	return text; 	
    }
    
	public void delete(String id) {
		Email savedEmail =  emailRepository.findById(id).orElseThrow(() -> new UPCException(ErrorCode.COMPANY_ID_NOT_FOUND, String.format("Company id not found '%s'.", id)));
    	if (savedEmail != null){
    		emailRepository.delete(savedEmail);
    	}	
	}

	public void sendEmail(Email email, String senderUserId, Boolean massEnabled) {
		
		
		
	/*	User user = userRepository.findOne(senderUserId);
    	if (user == null){
       		throw new UPCException(ErrorCode.USER_ID_NOT_FOUND, String.format("User id not found '%s'.", senderUserId));  		  
    	}*/
    	email.setFrom(from);
    	email.setStatus(EmailStatus.SENDING_IN_PROGRESS);
    	
		
		if (Boolean.FALSE.equals(massEnabled)){
			Pair<List<String>, List<String>> result = sendBulkEmail(email, 0);
	
        	if (email.getSuccessed() == null){
        		email.setSuccessed(new ArrayList<>());
        	}
        	email.getSuccessed().addAll(result.getLeft());
        	
        	if (email.getFailed() == null){
        		email.setFailed(new ArrayList<>());
        	}
        	email.getFailed().addAll(result.getRight());
        	
        	if ( (email.getSuccessed().size() + email.getFailed().size()) >= email.getTo().size()){
        		email.setStatus(EmailStatus.FINISH_SENDING);
        	}
		}
		emailRepository.save(email);
	  
	}
	
	
	
	public Pair<List<String>, List<String>> sendBulkEmail(Email email, int maxEmails ){
		
		
    	GridFsResource file = null;
    
    	
    	if (email.getAttachmentIds() != null && !email.getAttachmentIds().isEmpty()){
    		  file = fileService.getById(email.getAttachmentIds().get(0));
    	}

   	   
		 List<EmailBean> emailsBean = mapToEmailBean(email, file, maxEmails);
		
		return emailSenderService.send(emailsBean);
			 //LOGGER.error("Error occur while sending email to (" + bean.getTo() + ")" , e); 
		 //}
		/* if (emailsBean != null && sendEmailNumber < emailsBean.size()){
	      		throw new UPCException(ErrorCode.EMAILS_NOT_SENT_SUCCESSFULLY, "Some email are not send sucessfully.");  		  

		 }*/
	}
	
	
    private List<EmailBean>  mapToEmailBean(Email email, GridFsResource file, int maxEmails){
    	
    	List<EmailBean> emailsBean = new ArrayList<>();
    	byte[] binary = null;
    	if (file != null){
    	   binary = getBinaryFile(file);
    	}
    	int i = 0;
    	if (email.getTo() != null){
    		for(String to: email.getTo()){
    			
    			String receiverEmail = to;
    			String receiverName = "";
    			if (to.contains("(") && to.contains(")")){
    				receiverName = to.substring(0, to.indexOf("("));
    				receiverEmail = to.substring(to.indexOf("(") + 1, to.indexOf(")"));
    			}
    			if ((email.getSuccessed() != null && email.getSuccessed().contains(receiverEmail)) 
    					|| (email.getFailed() != null && email.getFailed().contains(receiverEmail))){
    				//Email already sent
 		        	continue;
 		        }
    			
    			EmailBean emailBean = new EmailBean();
    			String body = email.getBody();
    			
    			emailBean.setTo(Arrays.asList(receiverEmail));
				body = body.replaceAll("\\{receiverName\\}", receiverName);
    			emailBean.setSubject(email.getSubject());
		        emailBean.setMessage(body);
		        emailBean.setHtml(true);
		        emailBean.setFrom(email.getFrom());
			    if (file != null && binary != null){
				        emailBean.setAttachementBinary(binary);
				        emailBean.setAttachementFileName(file.getFilename());
				        emailBean.setAttachementFileType(file.getContentType());
			    }
		        emailsBean.add(emailBean);
		        i++;
		        if (maxEmails != 0 && i == maxEmails){
		        	break;
		        }
    		}
    	}
    
    	
    	return emailsBean;
    }
    
    private byte[] getBinaryFile(GridFsResource queryFile){
    	byte[] file = null;
    	if(queryFile != null){
    		try {
    			return toByteArray(queryFile.getInputStream());
    		} catch (IOException e) {
    			
    		}
    	}
    	return file;
    }

	public static byte[] toByteArray(InputStream in) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len;

		// read bytes from the input stream and store them in buffer
		while ((len = in.read(buffer)) != -1) {
			// write bytes from the buffer into output stream
			os.write(buffer, 0, len);
		}

		return os.toByteArray();
	}


     
   
}
