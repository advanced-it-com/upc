package com.isco.upc.security.annotations;

import javax.ws.rs.NameBinding;

import com.isco.upc.common.enums.UserPermission;
import com.isco.upc.common.enums.UserRole;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured {
	
	 UserRole[] roles() default {};
	 
	 UserPermission[] permissions() default {};
}