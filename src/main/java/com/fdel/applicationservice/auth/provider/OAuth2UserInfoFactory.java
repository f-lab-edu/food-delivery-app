package com.fdel.applicationservice.auth.provider;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fdel.exception.message.OAuth2UserRequestMessage;

/**
 * provider에 따라 적합한 OAuth2UserInfo의 서브타입을 만들어주는 객체이다.
 */
public class OAuth2UserInfoFactory {

	/**
	 * OAuth2UserRequest와 OAuth2User를 입력받아 
	 * Provider와 일치하는 OAuth2UserInfo의 서브타입을 만들어 반환한다.
	 * 
	 * @param userRequest OAuth2 인증 사이트로부터 받은 (사용자 정보 + 프로바이더) 정보이다. 
	 * @param oauth2User OAuth2 인증 사이트로부터 받은 사용자 정보이다.
	 * @return OAuth2UserInfo 공통된 인터페이스를 가진 OAuth2UserInfo를 구현한 사용자 정보 객체가 반환된다.
	 * @throws IllegalArgumentException 지원하지 않는 Provider가 제공했거나 잘못된 userRequest인 경우 던져진다. 
	 */
	static public OAuth2UserInfo getOAuth2UserInfo(OAuth2UserRequest userRequest
			, OAuth2User oauth2User)
	{
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if(userRequest.getClientRegistration().getRegistrationId()
				.equals("google")) {
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId()
				.equals("facebook")) {
			oAuth2UserInfo = new FaceBookUserInfo(oauth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId()
				.equals("naver")) {
			/* 
			 * 네이버 유저 정보는 response : {resultcode=00, message=success, 
			 * 		response={id= sadmsakda, email=someEmail@naver.com, name=홍길동}} 형태이다.
			 */
			oAuth2UserInfo= new NaverUserInfo((Map<String, Object>) oauth2User
				.getAttributes().get("response"));
		} else {
			throw new IllegalArgumentException(OAuth2UserRequestMessage
					.NOT_SUPPORTED_PROVIDER_REQUEST.getMessage());
		}
		
		return oAuth2UserInfo;
	}
	
}
