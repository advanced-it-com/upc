package com.isco.upc.app.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Provider
public class CROSFilter implements ContainerResponseFilter {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CROSFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres)
            throws IOException {
        cres.getHeaders().add("Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Headers", "access-control-allow-credentials,access-control-allow-headers,access-control-allow-methods,access-control-allow-origin,authorization,content-type,origin,accept");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        cres.getHeaders().add("Access-Control-Max-Age", "1209600");
        cres.getHeaders().add("Access-Control-Expose-Headers", "Content-Disposition");
    }
    


}
