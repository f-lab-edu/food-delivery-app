package com.fdel.controller.authmockconfig;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.fdel.entity.User;
import com.fdel.entity.User.Role;
import com.fdel.service.auth.PrincipalDetails;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        List<Role> roleList = Arrays.asList(customUser.roles());

        User mockUser = User.builder()
    		.username(customUser.username())
    		.password(customUser.password())
    		.email(customUser.email())
    		.roles(roleList).build();
        
        PrincipalDetails principalDetails = new PrincipalDetails(mockUser);
        Authentication authentication = 
    		new UsernamePasswordAuthenticationToken(
				principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());
        
        context.setAuthentication(authentication);
        return context;
    }
}