package com.isco.upc.app;


import com.isco.upc.app.domain.Company;
import com.isco.upc.app.domain.Person;
import com.isco.upc.app.domain.Skill;
import com.isco.upc.app.domain.User;
import com.isco.upc.app.repository.CompanyRepository;
import com.isco.upc.app.repository.FileStorageRepository;
import com.isco.upc.app.repository.PersonRepository;
import com.isco.upc.app.repository.UserRepository;
import com.isco.upc.common.enums.UserPermission;
import com.isco.upc.common.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



@SpringBootApplication
//@EnableAsync
//@EnableScheduling
//@ImportResource("file:**/mongo-config.xml")
public class UPCApplication {
	
	   /** The logger. */
 private static final Logger LOGGER = LoggerFactory.getLogger(UPCApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UPCApplication.class, args);
	}


	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UPCApplication.class);
    } */

    
	/*public static void main(String[] args) throws Exception {
		//SpringApplication.run(UPCApplication.class, args);
		LOGGER.debug("UPC app started successfully ....");
//"C:\\Users\\pankaj\\Desktop\\client.json"
		String data = readFileAsString("D:\\workspace\\micro-services\\upc-backend\\upc-app\\src\\main\\resources\\clients.json");
	    System.out.println(data);
	    
	    ObjectMapper mapper = new ObjectMapper();
	    
	    CollectionType javaType = mapper.getTypeFactory()
	    	      .constructCollectionType(List.class, Company.class);
	    	    List<Company> companies = mapper.readValue(data, javaType);
	    
	  
	    	    
	    	    try (
	    	            Writer writer = Files.newBufferedWriter(Paths.get("contacts.csv"));

	    	            CSVWriter csvWriter = new CSVWriter(writer,
	    	                    CSVWriter.DEFAULT_SEPARATOR,
	    	                    CSVWriter.NO_QUOTE_CHARACTER,
	    	                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
	    	                    CSVWriter.DEFAULT_LINE_END);
	    	        ) {
	    	            String[] headerRecord = {"Company Name", "Company Email", "Company Phone", "Company Address", "Contact First Name", "Contact Last Name", "Contact Email", "Mailing Enabled"};
	    	            csvWriter.writeNext(headerRecord);
	    	            
	    	            for(Company c: companies){
	    	            	
	    	            	for(Contact n: c.getContacts()){
	    	    	            csvWriter.writeNext(new String[]{c.getName(), c.getEmail(), c.getPhone(), c.getAddress()== null ? "" : c.getAddress().getAddress(),
	    	    	            		
	    	    	               n.getFirstName(), n.getLastName(), n.getEmail(),
	    	    	               
	    	    	               n.getEmailDisabled() == null  || n.getEmailDisabled().equals(Boolean.FALSE) ?	"FALSE"	 : "TRUE"
	    	    	            });

	    	            	}
	    	            }

	    	        }
	    	    LOGGER.debug("Finish writing");
	}*/
	
	
	public static String readFileAsString(String fileName)throws Exception
	  {
	    String data = "";
	    data = new String(Files.readAllBytes(Paths.get(fileName)));
	    return data;
	  }
	 
	 
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	FileStorageRepository fileStorageRepository;
	
	Random r = new Random();



	@Bean
	public CommandLineRunner init() {
		return new CommandLineRunner() {
			@Override
			public void run(String... arg0) throws Exception {
			    if (userRepository.count() == 0){



                  /*  DBObject metaData = new BasicDBObject();
                     metaData.put("brand", "Audi");
                     metaData.put("model", "Audi A3");
                     metaData.put("description","Audi german automobile manufacturer that designs, engineers, and distributes automobiles");

                     InputStream stream = this.getClass().getClassLoader().getResourceAsStream("default-avatar.png");
                     if (stream != null){
                        String imageId = fileStorageRepository.store(stream, "default-avatar.png", "png", metaData);
                        System.out.println("Image Stored with Id: "+ imageId);
                     }*/

                    String companyId = companyRepository.save(createCompany()).getId();
                    getDefaultUsers(companyId).stream().forEach(u -> {
                        userRepository.save(u);
                        System.out.println("User Saved:" + u);
                    });
                    getDefaultPerson().stream().forEach(p -> {
                        personRepository.save(p);
                        System.out.println("person Saved:" + p);
                    });

              }
				
			}

//			private Store[] getStores() {
//				Store[] s = new Store[3];
//				for (int i = 0; i < s.length; i++) {
//					s[i] = new Store(getStoreName(), getStoreAddress());
//				}
//				return s;
//			}
//
//			private String getStoreAddress() {
//				return "Infinite Loop, California, US";
//			}
//
//			private String getStoreName() {
//				return "StoreName:" + r.nextInt(10000);
//			}
//			
			private List<User> getDefaultUsers(String companyId) {
				return Arrays.asList( new User(companyId, "asamet", "toutou", "abdessalem.samet@dynamix-it.be", new Date(), Arrays.asList(UserRole.ADMIN), Arrays.asList(UserPermission.VIEW_RATE)),
				 new User(companyId, "collard", "moumou", "collard@gmail.com", new Date(), Arrays.asList(UserRole.CONSULTANT), Arrays.asList(UserPermission.FILL_TIMESHEET)));
			
			}
			
			private List<Person> getDefaultPerson() {
				
				
				Person p1 = new Person( "abdessalem.samet@dynamix-it.be", "Abdessalem", "SAMET");
				p1.setJob("Devloppeur java");
				p1.setYearsExperience(5);
				p1.setPhone("+878798989");
				
				List<Skill> skills = new ArrayList<>();
				Skill s1 = new Skill();
				s1.setName("Angular 2");
				s1.setYears(5);
				s1.setLevel(2);
				skills.add(s1);
				
				Skill s2 = new Skill();
				s2.setName("Angular 2");
				s2.setYears(5);
				skills.add(s2);
			
				p1.setSkills(skills);
				
				Person p2 = new Person("collard@gmail.com", "asamet", "toutou");
				p2.setJob("Director");
				p2.setYearsExperience(3);
				return Arrays.asList( p1, p2);
			
			}
			
			private Company createCompany() {
				return new Company("Dynamix-SA", "hatem@dynamix.be");			
			}
		};
	}
}
