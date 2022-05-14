package com.fdel.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fdel.controller.authmockconfig.WithMockAnonymous;
import com.fdel.controller.authmockconfig.WithMockCustomUser;
import com.fdel.controller.authmockconfig.WithMockOrderer;
import com.fdel.controller.authmockconfig.WithMockStoreOwner;
import com.fdel.entity.User.Role;

@SpringBootTest
class AuthorizationTestControllerTest {

	@Autowired
	private AuthorizationTestController authorizationTestController;
	
	@Autowired
	FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private SpringSessionBackedSessionRegistry<? extends Session> springSessionBackedSessionRegistry;
	
	private MockMvc mock;
	
	@BeforeEach
	void setup() {
		mock = MockMvcBuilders
			.standaloneSetup(authorizationTestController)
			.apply(springSecurity(springSecurityFilterChain))
			.setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
			.build();
	}
	
	@Test
	@WithMockAnonymous
	@DisplayName("Anomymous 유저가 /authentication 요청시 OK응답 및 인증된 유저 정보 출력")
	void when_requestAuthenticationByAnomymous_then_reponseOkAndPrintAuthenticationUserInfo() 
			throws Exception {
		mock.perform(get("/authentication"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockOrderer
	@DisplayName("ORDERER 권한 유저가 /authentication 요청시 OK응답 및 인증된 유저 정보 출력")
	void when_requestAuthenticationByOrderer_then_reponseOkAndPrintAuthenticationUserInfo() 
			throws Exception {
		mock.perform(get("/authentication"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("STOREOWNER 권한 유저가 /authentication 요청시 OK응답 및 인증된 유저 정보 출력")
	void when_requestAuthenticationByStoreOwner_then_reponseOkAndPrintAuthenticationUserInfo() 
			throws Exception {
		mock.perform(get("/authentication"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockOrderer
	@DisplayName("ORDERER 유저가 orderer만 접근 가능한 자원 요청시 OK 응답")
	void when_requestOnlyOrdererResoueceByOrderer_then_reponseOk() 
			throws Exception {
		mock.perform(get("/orderer"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("STOREOWNER 유저가 orderer만 접근 가능한 자원 요청시 접근 제한 응답")
	void when_requestOnlyOrdererResoueceByStoreOwner_then_reponseForbidden() 
			throws Exception {
		mock.perform(get("/orderer"))
			.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockAnonymous
	@DisplayName("ANONYMOUS 유저가 orderer만 접근 가능한 자원 요청시 로그인 페이지로 리다이렉트")
	void when_requestOnlyOrdererResoueceByAnonymous_then_redirectToLoginForm() 
			throws Exception {
		mock.perform(get("/orderer"))
			.andExpect(status().is3xxRedirection())
			.andDo(print())
			.andExpect(header().string("Location", "http://localhost/loginForm"));
	}
	
	@Test
	@WithMockAnonymous
	@DisplayName("ANONYMOUS 유저가 anonymous만 접근 가능한 자원 요청시 OK 응답")
	void when_requestOnlyAnonymousResoueceByOrderer_then_reponseOk() 
			throws Exception {
		mock.perform(get("/anonymous"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockOrderer
	@DisplayName("ORDERER 권한 유저가 anonymous만 접근 가능한 자원 요청시 접근 제한 응답")
	void when_requestOnlyAnonymousResoueceByOrderer_then_reponseForbidden()
			throws Exception {
		mock.perform(get("/anonymous"))
			.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockCustomUser(roles = {Role.ORDERER, Role.STORE_OWNER})
	@DisplayName("ORDERER, STOREOWNER 권한 유저가 ORDERER만 접근 가능한 자원 요청시 OK 응답")
	void when_requestOnlyOrdererResoueceByOrdererAndStoreOwner_thenResponseOk() 
			throws Exception {
		mock.perform(get("/info/orderer"))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockCustomUser(roles = {Role.ORDERER, Role.STORE_OWNER})
	@DisplayName("ORDERER, STOREOWNER 권한 유저가 STOREOWNER만 접근 가능한 자원 요청시 OK 응답")
	void when_requestOnlyStoreOwnerResoueceByOrdererAndStoreOwner_thenResponseOk() 
			throws Exception {
		mock.perform(get("/info/storeowner"))
			.andExpect(status().isOk());
	}		
	
}
