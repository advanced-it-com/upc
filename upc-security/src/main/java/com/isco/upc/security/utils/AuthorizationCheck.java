package com.isco.upc.security.utils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.enums.UserPermission;
import com.isco.upc.common.enums.UserRole;
import com.isco.upc.common.exceptions.UPCException;
import com.isco.upc.security.annotations.Secured;

public class AuthorizationCheck {

	public static void checkRoleAndPermissions( Class<?> resourceClass,  Method resourceMethod, List<UserRole> roles, List<UserPermission> permissions){

        List<UserRole> classRoles = extractRoles(resourceClass);
        List<UserPermission> classPermissions = extractPermissions(resourceClass);
        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        List<UserRole> methodRoles = extractRoles(resourceMethod);
        List<UserPermission> methodPermissions = extractPermissions(resourceMethod);

   
            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkRoles(classRoles, roles);
            } else {
            	checkRoles(methodRoles,  roles);
            }
            
            
            if (methodRoles.isEmpty()) {
                checkPermissions(classPermissions, permissions);
            } else {
                checkPermissions(methodPermissions,  permissions);
            }
      
	}
    // Extract the roles from the annotated element
    private static List<UserRole> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<UserRole>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<UserRole>();
            } else {
                UserRole[] allowedRoles = secured.roles();
                return Arrays.asList(allowedRoles);
            }
        }
    }
    
    // Extract the roles from the annotated element
    private static List<UserPermission> extractPermissions(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<UserPermission>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<UserPermission>();
            } else {
                UserPermission[] allowedPermissions = secured.permissions();
                return Arrays.asList(allowedPermissions);
            }
        }
    }


    private static void checkRoles(List<UserRole> allowedRoles, List<UserRole> userRoles){
    	
    	
    //	userRoles.stream().filter(ur -> allowedRoles.contains(ur)).
    	if (!allowedRoles.isEmpty() && allowedRoles.stream().anyMatch(ur -> userRoles.contains(ur)) == false){
    		 throw new UPCException(ErrorCode.FORBIDDEN, "FORBIDDEN: Operation not granted.");
    	}
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
    }
    
    private static void checkPermissions(List<UserPermission> allowedPermissions, List<UserPermission> userPermissions) {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
    	if (!allowedPermissions.isEmpty() && allowedPermissions.stream().anyMatch(ur -> userPermissions.contains(ur)) == false){
    		 throw new UPCException(ErrorCode.FORBIDDEN, "FORBIDDEN: Operation not granted.");
    	}
    }
}
