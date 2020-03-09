package com.isco.upc.app.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.isco.upc.app.email.beans.PersonFunction;

@Document
public class Person {

	@Id
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private String skype;
	private String photoFileId;
	
	private PersonFunction personfunction;
	
	private String status;

	
	//@NotNull
	private String job;
	private Integer yearsExperience;
	private String education;
	private String homeAddress;
	private String jobAddress;
	private String certifications;
	private List<Skill> skills;
	private String mission;
	private String rate;
	@DateTimeFormat(style = "M-")
    private Date lastCvUpdate;
	private String cvFileId;	
	@DateTimeFormat(style = "M-")
    private Date workPermitExpirationDate;
	
	@DateTimeFormat(style = "M-")
    private Date startingDateDate;
	
	
	private List<History> histories;
	
	private List<PersonDocument> documents;
	
	
	
	
	public Person(){}
	public Person(String email, String firstName, String lastName){
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getPhotoFileId() {
		return photoFileId;
	}



	public void setPhotoFileId(String photoFileId) {
		this.photoFileId = photoFileId;
	}



	public String getJob() {
		return job;
	}



	public void setJob(String job) {
		this.job = job;
	}



	public Integer getYearsExperience() {
		return yearsExperience;
	}



	public void setYearsExperience(Integer yearsExperience) {
		this.yearsExperience = yearsExperience;
	}



	public String getEducation() {
		return education;
	}



	public void setEducation(String education) {
		this.education = education;
	}



	public String getHomeAddress() {
		return homeAddress;
	}



	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}



	public String getJobAddress() {
		return jobAddress;
	}



	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}




	public String getCertifications() {
		return certifications;
	}
	public void setCertifications(String certifications) {
		this.certifications = certifications;
	}
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public List<Skill> getSkills() {
		return skills;
	}



	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}



	public Date getLastCvUpdate() {
		return lastCvUpdate;
	}



	public void setLastCvUpdate(Date lastCvUpdate) {
		this.lastCvUpdate = lastCvUpdate;
	}



	public String getCvFileId() {
		return cvFileId;
	}



	public void setCvFileId(String cvFileId) {
		this.cvFileId = cvFileId;
	}



	public Date getWorkPermitExpirationDate() {
		return workPermitExpirationDate;
	}



	public void setWorkPermitExpirationDate(Date workPermitExpirationDate) {
		this.workPermitExpirationDate = workPermitExpirationDate;
	}
	public Date getStartingDateDate() {
		return startingDateDate;
	}
	public void setStartingDateDate(Date startingDateDate) {
		this.startingDateDate = startingDateDate;
	}


	public PersonFunction getPersonfunction() {
		return personfunction;
	}
	public void setPersonfunction(PersonFunction personfunction) {
		this.personfunction = personfunction;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<History> getHistories() {
		return histories;
	}
	public void setHistories(List<History> histories) {
		this.histories = histories;
	}
	public List<PersonDocument> getDocuments() {
		return documents;
	}
	public void setDocuments(List<PersonDocument> documents) {
		this.documents = documents;
	}
	
	
	
	
}


