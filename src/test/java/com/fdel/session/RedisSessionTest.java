package com.fdel.session;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fdel.applicationservice.auth.PrincipalDetails;
import com.fdel.controller.authmockconfig.WithMockCustomUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class RedisSessionTest {
	
	@Autowired
	private SpringSessionBackedSessionRegistry<? extends Session> springSessionBackedSessionRegistry;
	
	@Autowired
	private WebApplicationContext applicationContext;
	
	@Autowired
	private SessionRepositoryFilter sessionRepositoryFilter;
	
	@Autowired
	FilterChainProxy springSecurityFilterChain;
	
	private MockMvc mock;

	@BeforeEach
	void beforeEach() {
		mock = MockMvcBuilders
			.webAppContextSetup(applicationContext)
			.addFilter(sessionRepositoryFilter)
			.apply(springSecurity(springSecurityFilterChain))
			.build();
	}
	
	//mockMvc에 SpringSecurity의 FilterChainFroxy를 넣고싶으면 .apply(springSecurity())를 해줘야 한다.
	//이 테스트는 눈으로 출력을 확인하기 위한 테스트입니다.
	@Test
	@WithMockCustomUser
	@DisplayName("mockUser 사용자의 인증 객체가 들어있는 securitycontext 객체가 할당되면 redis에 session 정보가 저장되어야 한다.")
	void redis_session_information_print_test() throws Exception {
		MvcResult result = 
			mock.perform(get("/sessionid")).andExpect(status().isOk()).andReturn();
		MockHttpSession session = 
			(MockHttpSession)result.getRequest().getSession();
		
		assertThat(session, is(notNullValue()));
		
		mock.perform(get("/sessionid").session(session)).andExpect(status().isOk());
		
		Authentication authentication = 
			TestSecurityContextHolder.getContext().getAuthentication();
		
		log.info("==============Redis Session test=============");
		
		if(authentication == null) {
			log.info("test에서 authentication 객체가 null 입니다.");
		} else {
			
			Object principal = authentication.getPrincipal();
			PrincipalDetails principalDetails = (PrincipalDetails)principal;
			
			log.info("Security Context에 있는 인증 객체 유저이름 : {}", principalDetails.getUsername());
			
			List<SessionInformation> sessionInformations = 
				springSessionBackedSessionRegistry.getAllSessions(principal, false);
			
			if(sessionInformations.isEmpty()) {
				log.info("redis session 정보가 비었습니다!");
			}	
			for(SessionInformation sessionInformation : sessionInformations) {
				log.info("유저 이름은 : {}", (String)sessionInformation.getPrincipal());
			}
		}		
	}
	
	@Test
	@DisplayName("redis에 저장된 인증된 ORDERER 유저 세션 정보를 가지고 ORDERER 권한이 필요한 페이지로 접근하면 로그인할 필요 없이 접근에 성공한다.")
	void when_accessPageThatRequiresORDERERPrivilegesWithAuthenticatedORDERERUserSessionIdStoredInRedis_then_responseSuccessful() 
			throws Exception {
		MvcResult result = mock
	    .perform(get("/orderer").with(user("mockOrderer").password("mockPassword").roles("ORDERER")))
	    .andReturn();
		Cookie cookie = result.getResponse().getCookie("SESSION");
		mock.perform(get("/orderer").cookie(cookie)).andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("redis에 저장된 인증된 ORDERER 유저 세션 정보를 가지고 STORE_OWNER 권한이 필요한 페이지로 접근하면 권한 없음 403 애러가 페이지를 보여준다.")
	void when_accessPageThatRequiresSTORE_OWNERPrivilegesWithAuthenticatedORDERERUserSessionIdStoredInRedis_then_response403ClientError() 
			throws Exception {
		MvcResult result = mock
	    .perform(get("/orderer").with(user("mockOrderer").password("mockPassword").roles("ORDERER")))
	    .andReturn();
		Cookie cookie = result.getResponse().getCookie("SESSION");
		mock.perform(get("/storeowner").cookie(cookie))
			.andExpect(status().isForbidden()); //403 forbidden
	}
	
}
