package com.isco.upc.app.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.isco.upc.app.email.beans.EmailStatus;

@Document
public class Email {

	@Id
	private String id;
	private String name;
	private String type;
	private Date creation;
	private String body;
	private String subject;
	private String from;
	private List<String> to;
	private List<String> attachmentIds;
	
	private EmailStatus status;
	private List<String> successed;
	private List<String> failed;
	

	
	public Email() {

	}
	

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public Date getCreation() {
		return creation;
	}



	public void setCreation(Date creation) {
		this.creation = creation;
	}



	public String getBody() {
		return body;
	}



	public void setBody(String body) {
		this.body = body;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getFrom() {
		return from;
	}



	public void setFrom(String from) {
		this.from = from;
	}



	public List<String> getTo() {
		return to;
	}



	public void setTo(List<String> to) {
		this.to = to;
	}



	public List<String> getAttachmentIds() {
		return attachmentIds;
	}



	public void setAttachmentIds(List<String> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}



	public EmailStatus getStatus() {
		return status;
	}



	public void setStatus(EmailStatus status) {
		this.status = status;
	}



	public List<String> getSuccessed() {
		return successed;
	}



	public void setSuccessed(List<String> successed) {
		this.successed = successed;
	}



	public List<String> getFailed() {
		return failed;
	}



	public void setFailed(List<String> failed) {
		this.failed = failed;
	}

	
	

}
