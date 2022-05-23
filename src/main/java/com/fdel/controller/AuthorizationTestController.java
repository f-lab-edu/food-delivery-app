package com.fdel.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdel.service.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증 인가 테스트용 컨트롤러입니다.
 * 나중에 삭제될 예정입니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorizationTestController {
	
	@Autowired
	private SpringSessionBackedSessionRegistry<? extends Session> springSessionBackedSessionRegistry;
	
	//redis session을 테스트하기 위한 함수입니다.
	@GetMapping("/sessionid")
	@ResponseBody
	public String getSessionId(HttpSession httpSession) {
		
		List<SessionInformation> allSessions = new ArrayList<>();
		
		 log.info("========= redis Session test controller==========");
		 log.info("세션 maxInactiveInterval={}", httpSession.getMaxInactiveInterval());

		// (수동 테스트 확인용) 세션에 임의의 데이터 설정
		// httpSession.setAttribute("someAttribute", "someValue");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null) {
			log.info("Authentication 객체가 null 입니다.");
		} else {
			PrincipalDetails principal = (PrincipalDetails)authentication.getPrincipal();
		log.info("Autentication 객체의 이름을 출력합니다. : {}", principal.getUsername());
	       allSessions = springSessionBackedSessionRegistry
    		   .getAllSessions(authentication.getPrincipal(), false); //true로 설정하면 만료된 세션도 볼 수 있습니다.
		}
        
        if(allSessions.isEmpty()) {
        	log.info("요청시 redis 세션 정보가 비었습니다!");
        } else {
    	  for(SessionInformation sessionInfo : allSessions) {
          	log.info("redis에 저장된 세션 id : {}", sessionInfo.getSessionId());
          }
        }
        
        return httpSession.getId();
	}
	
	
	/*
	 * principalDetails은 UserDetails과 OAuth2User 모두 구현했기 때문에
	 * 슈퍼 타입과 관계없이 인증 정보를 DI 받을 수 있다. 
	 */
	@GetMapping("/authentication")
	@ResponseBody
	public String loginTest(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails principalDetails) { 
		
		if(authentication != null && principalDetails != null){
			PrincipalDetails principalDetails2 = (PrincipalDetails) authentication.getPrincipal();
			
			String[] authorites= authentication
					.getAuthorities().stream().map(e->e.getAuthority()).toArray(String[]::new);
			
			log.info("============App=============");
			log.info("유저 권한 : {}", String.join(", ", authorites));
			log.info("authentication : {}", principalDetails2.getUser());
			log.info("principalDetails : {}", principalDetails.getUser());
			return "세션 정보 확인";
		} 
		
		/*
		 * anonymous 유저인 경우 controller의 파라미터로 Authentication 객체와 principal 객체를 주입받지 못한다.
		 * 
		 * anonymous 유저인 경우 Authentication 객체를 주입받지 못하도록 한 이유는 표준 서플릿 API 스펙을 따라가기 위해서라고
		 * 한다. 아래는 링크에서 가져온 설명이다. 
		 * 
		 * 서블릿 API의 요점은 Spring Security와 독립적이고 문서화된 표준 동작을 가지고 있다는 것입니다. 
		 * 기존 응용 프로그램은 해당 동작에 의존할 수 있으므로 (익명 사용자인 경우 인증객체를 null로 반환하는 방식을)변경하지 않을 것입니다. 
		 * 애플리케이션이 Spring Security를 ​​사용하고 있음을 알고 있으면 Spring Security API를 사용하여 보안 컨텍스트에 액세스할 수 있습니다. 
		 * AnonymousAuthenticationFilter의 사용은 Spring Security의 관점에서 "누군가가 인증되었다"는 것을 의미하지 않습니다. 
		 * 인증 되지 않은 액세스는 보안 컨텍스트에서 익명 토큰으로 표시됩니다. 
		 * 이것이 원래 도입된 이유는 보안 애플리케이션 컨텍스트에서 액세스 제어 구성을 단순화하기 위한 것입니다.
		 *  
		 *  만약 controller에서 Anonymous의 인증 정보를 사용하고 싶다면 
		 *  아래와 같이 Spring Security API를 사용해서 꺼내와야 한다.
		 *  authentication = SecurityContextHolder.getContext().getAuthentication(); 
		 *  
		 * 참조 https://github.com/spring-projects/spring-security/issues/4011
		 * 참조 https://github.com/spring-projects/spring-security/issues/1333
		 * 참조 https://www.inflearn.com/questions/71433
		 */
		
		authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String[] authorites = 
			authentication.getAuthorities().stream()
			.map(e->e.getAuthority()).toArray(String[]::new);
		
		log.info("============Anonymous=============");
		log.info("유저 권한 : {}", String.join(", ", authorites));
		
		return "먼저 로그인해서 인증을 해주세요";
	}
	
	/* 여기서 authentication.getPrincipal()로 받아지는 객체도
	 * oAuth2User 타입으로 받아지는 객체도
	 * PrincipalOauth2UserService의 loadUser 함수가 반환한
	 * PincipalDetails 객체이다.
	 * 
	 * 원래 OAuth2 인증시 authentication에 저장되는
	 * 인증 정보 타입은 OAuth2User 타입이지만
	 * 이 어플리케이션에서는 일반 로그인 인증 타입 UserDetails와
	 * OAuth2 인증 타입 OAuth2User을 같은 인터페이스로 다루기 위해
	 * PincipalDetails라는 서브 타입으로 감싸서 사용한다.
	 */
	@GetMapping("/OAuth/authentication")
	@ResponseBody
	public String appLoginTest(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oAuth2User) { 
	
		if(authentication != null && oAuth2User != null){
			OAuth2User oAuth2User2 = (OAuth2User) authentication.getPrincipal();
			log.info("============OAuth=============");
			log.info("authentication : {}", oAuth2User2.getAttributes());
			log.info("oAuth2User : {}", oAuth2User.getAttributes());
			return "OAuth 세션 정보 확인";
		}
		
		return "먼저 OAuth 로그인해서 인증을 해주세요";
	}
	
	@ResponseBody
	@GetMapping("/orderer")
	public String admin() {
		return "orderer 권한이 필요한 페이지입니다.";
	}
	
	@ResponseBody
	@GetMapping("/storeowner")
	public String manager() {
		return "storeowner 권한이 필요한 페이지입니다.";
	}
	
	@Secured("ROLE_ANONYMOUS")
	@GetMapping("/anonymous")
	public @ResponseBody String onlyAnomymous() {
		return "anonymous만 들어올 수 있는 페이지입니다.";
	}
	
	/*
	 * 시큐리티 설정에서 전역적으로 권한 설정을 잡아주고나서
	 * 메소드 레벨에서 세부적인 권한 설정을 추가하고 싶을 때 
	 * 이런 애노테이션을 사용하면 된다.
	 * 메소드 레벨에서는 Secured 애너테이션을 주로 사용한다.
	 */
	@Secured("ROLE_ORDERER")
	@GetMapping("/info/orderer")
	public @ResponseBody String ordererInfo() {
		return "orderer 개인정보";
	}
	
	@Secured("ROLE_STORE_OWNER")
	@GetMapping("/info/storeowner")
	public @ResponseBody String restrantOwnerInfo() {
		return "restrantowner 개인정보";
	}
	
	/*
	 * 메소드 레벨에서 여러 권한 체크를 하고 싶을 때는 
	 * @PreAuthorize를 사용한다.
	 */
	@PreAuthorize("hasRole('ROLE_ORDERER') or hasRole('ROLE_STORE_OWNER')")
	@GetMapping("/info")
	public @ResponseBody String data() {
		return "user 개인정보";
	}
	
}
