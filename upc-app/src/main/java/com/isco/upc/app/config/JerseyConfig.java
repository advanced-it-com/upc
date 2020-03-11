package com.isco.upc.app.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.context.annotation.Configuration;

import com.isco.upc.app.exceptionmapper.UPCExceptionMapper;
import com.isco.upc.app.filter.CROSFilter;
import com.isco.upc.app.filter.JWTRequestFilter;
import com.isco.upc.app.resource.AuthenticationResource;
import com.isco.upc.app.resource.CompanyResource;
import com.isco.upc.app.resource.EmailResource;
import com.isco.upc.app.resource.FileResource;
import com.isco.upc.app.resource.PersonResource;
import com.isco.upc.app.resource.UserResource;


@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		registerEndpoints();
	//	configureSwagger();
	}

/*	private void configureSwagger() {
		register(ApiListingResource.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/api");
		beanConfig.setResourcePackage("com.isco.upc.app");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
	}*/

	private void registerEndpoints() {
	//	 packages("com.isco.upc.app");
		 
	//	register(WadlResource.class);
		register(FileResource.class);
		register(AuthenticationResource.class);
		register(UserResource.class);
		register(PersonResource.class);
		register(CompanyResource.class);
		register(EmailResource.class);
		 
	    register(MultiPartFeature.class);
	//	register(MultipartResolver.class);
		
		
		register(CROSFilter.class);
		register(JWTRequestFilter.class);
		
	//	register(ApiListingResource.class);
	//	register(SwaggerSerializers.class);
		register(UPCExceptionMapper.class);
		
		
		
	}
}
