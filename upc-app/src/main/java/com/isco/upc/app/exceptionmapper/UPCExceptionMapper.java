package com.isco.upc.app.exceptionmapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.isco.upc.app.utils.ErrorLoader;
import com.isco.upc.app.utils.UPCError;
import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.exceptions.UPCException;
import com.isco.upc.common.utils.ErrorUtil;

@Provider
public class UPCExceptionMapper implements ExceptionMapper<UPCException> {
	
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UPCExceptionMapper.class);
    
	@Autowired
	ErrorLoader errorLoader;

	@Override
	public Response toResponse(UPCException exception) {
		
		//Trace Error
		LOGGER.error(ErrorUtil.extractExceptionMessage(exception));
		ErrorCode errorCode = exception.getCode();
		if (ErrorCode.FORBIDDEN.equals(errorCode)){
			return Response.status(Response.Status.FORBIDDEN).entity(exception.getMessage()).type("text/html").build();
		}
		
		if (ErrorCode.UNAUTHORIZED.equals(errorCode)){
			return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).type("text/html").build();
		}
		
		UPCError se = errorLoader.getUPCError(errorCode.getCode());
		return Response.status(se.getStatus()).entity(se).type("application/json").build();
	}
}
