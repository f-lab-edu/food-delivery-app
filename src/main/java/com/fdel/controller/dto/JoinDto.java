package com.fdel.controller.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fdel.entity.User;
import com.fdel.entity.User.Role;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class JoinDto {
	
	String username;
	String password;
	String email;
	
	public User toUser(PasswordEncoder passwordEncoder) {
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPassword(passwordEncoder.encode(password));
		newUser.setEmail(email);
		newUser.setRoles(new ArrayList<Role>(Arrays.asList(Role.ORDERER))); //default
		return newUser;
	}
	
}

