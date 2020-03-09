package com.isco.upc.app.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Project {


	@Id
	private String projectId;
	
	private String name;
	private String shortDescription;
	private String description;
	private String status;
	
	private String category;
	private List<String> sectors;
	
	private String startDate;
	private String endDate;
	
	
	private String managerPersonId;
	
	private List<TeamMember> team;
	
	private String budget;
	
	private String logoId;
	
	private List<String> technologies;
	
	
	private List<String> organisationsId;
	
	
	class TeamMember{
		
		private String personId;
		private String position;
	}
	

}
