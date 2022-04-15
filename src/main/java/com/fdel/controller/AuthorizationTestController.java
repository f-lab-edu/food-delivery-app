package com.fdel.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdel.applicationservice.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 인증 그리고 인가에 대한 테스트 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorizationTestController {
	
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
			log.info("============App=============");
			log.info("authentication : {}", principalDetails2.getUser());
			log.info("principalDetails : {}", principalDetails.getUser());
			return "세션 정보 확인";
		} 
		
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
	@GetMapping("/restrantowner")
	public String manager() {
		return "restrantowner 권한이 필요한 페이지입니다.";
	}
	
	/*
	 * 시큐리티 설정에서 전역적으로 권한 설정을 잡아주고나서
	 * 메소드 레벨에서 세부적인 권한 설정을 추가하고 싶을 때 
	 * 이런 애노테이션을 사용하면 된다.
	 * 메소드 레벨에서는 Secured 애너테이션을 주로 사용한다.
	 */
	@Secured("ROLE_ORDERER")
	@GetMapping("/ordererInfo")
	public @ResponseBody String ordererInfo() {
		return "orderer 개인정보";
	}
	
	@Secured("ROLE_RESTRANTOWNER")
	@GetMapping("/restantOwnerInfo")
	public @ResponseBody String restrantOwnerInfo() {
		return "restrantowner 개인정보";
	}
	
	/*
	 * 메소드 레벨에서 여러 권한 체크를 하고 싶을 때는 
	 * @PreAuthorize를 사용한다.
	 */
	@PreAuthorize("hasRole('ROLE_ORDERER') or hasRole('ROLE_RESTRANTOWNER')")
	@GetMapping("/info")
	public @ResponseBody String data() {
		return "유저 개인 정보";
	}
	
}
