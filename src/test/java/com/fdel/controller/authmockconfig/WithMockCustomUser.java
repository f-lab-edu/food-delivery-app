package com.fdel.controller.authmockconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

import com.fdel.entity.User.Role;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

	String username() default "mockUser";
	String password() default "mockPassword";
	String email() default "mock@email";
	Role[] roles() default {Role.ORDERER}; 
}
