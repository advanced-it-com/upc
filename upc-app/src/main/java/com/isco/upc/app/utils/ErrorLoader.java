package com.isco.upc.app.utils;



import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
@PropertySource("classpath:errors.properties")
public class ErrorLoader {
	@Autowired
	Environment env;

	public UPCError getUPCError(String code) {
		UPCError se = new UPCError();
		se.setCode(code);
//		final String prefix = ERROR + DOT + code + DOT;
//		se.setDescription(env.getProperty(prefix + DESCRIPTION));
//		se.setModule(env.getProperty(prefix + MODULE));
//		se.setSeverity(env.getProperty(prefix + SEVERITY));
		se.setStatus(Response.Status.BAD_REQUEST);
		return se;
	}
}
