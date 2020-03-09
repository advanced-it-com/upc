package com.isco.upc.app.email.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.isco.upc.app.email.beans.EmailBean;
import com.isco.upc.app.email.template.EmailTemplate;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class ConsultantCompanyEmailService {
	
	@Autowired
	EmailSenderService emailService;
	
    @Autowired
    private Configuration freemarkerConfig;

	
	public void sendToCompanyConsultantDetail() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException{

			//Ex: https://www.quickprogrammingtips.com/spring-boot/how-to-send-email-from-spring-boot-applications.html
		    //http://www.opencodez.com/java/java-mail-framework-using-spring-boot.htm
 
		String from = "pavan@localhost";
		List<String> to = Arrays.asList("solapure@localhost");
		String subject = "Java Mail with Spring Boot";
		 
		EmailTemplate template = new EmailTemplate("company-consultant.html");
		 
		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("user", "Pavan");
		replacements.put("today", String.valueOf(new Date()));
		
		
		  Map<String, Object> model = new HashMap();
	        model.put("user", "qpt");
	        
	        // set loading location to src/main/resources
	        // You may want to use a subfolder such as /templates here
	        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
	        
	        Template t = freemarkerConfig.getTemplate("welcome.ftl");
	        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

		 
		String message = template.getTemplate(replacements);
		 
		EmailBean email = new EmailBean(from, to, subject, message);
		email.setHtml(true);
		emailService.send(email);
	}

}
