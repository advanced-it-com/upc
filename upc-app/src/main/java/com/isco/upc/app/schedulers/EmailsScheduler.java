/*package com.isco.upc.app.schedulers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.isco.upc.app.domain.Email;
import com.isco.upc.app.email.beans.EmailStatus;
import com.isco.upc.app.email.service.EmailSenderService;
import com.isco.upc.app.repository.EmailRepository;
import com.isco.upc.app.service.EmailService;
import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.exceptions.UPCException;

@Component
public class EmailsScheduler {
	

	private static final Logger logger = LoggerFactory.getLogger(EmailsScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private EmailRepository emailRepository;
    
    
    @Value("${mail.from}")
    private String from; // = "amira@dynamix-it.be";
    
    @Value("${mail.max}")
    private String maxEmail;

    //@Scheduled(fixedRate = 30000)  // every 30 seconds
    @Scheduled(cron="0 0/5 * * * ?")
    public void reportCurrentTime() {
        logger.info("The time is now {}", dateFormat.format(new Date()));
        
        List<Email> emails = emailRepository.findByStatus(EmailStatus.SENDING_IN_PROGRESS);
        if (emails == null || emails.isEmpty()){
        	 logger.debug("No email retreived by cron {}", dateFormat.format(new Date()));
        }else{
       	     logger.debug("{} emails retreived by cron {}", emails.size(), dateFormat.format(new Date()));
       	    for(Email em: emails){
            	
            	em.setFrom(from);
            	Pair<List<String>, List<String>> result = emailService.sendBulkEmail(em, new Integer(maxEmail));
            	if (em.getSuccessed() == null){
            		em.setSuccessed(new ArrayList<>());
            	}
            	em.getSuccessed().addAll(result.getLeft());
            	
            	if (em.getFailed() == null){
            		em.setFailed(new ArrayList<>());
            	}
            	em.getFailed().addAll(result.getRight());
            	
            	if ( (em.getSuccessed().size() + em.getFailed().size()) >= em.getTo().size()){
            		em.setStatus(EmailStatus.FINISH_SENDING);
            	}
            	emailRepository.save(em);
            }
        }
        
    

       
    }

}
*/