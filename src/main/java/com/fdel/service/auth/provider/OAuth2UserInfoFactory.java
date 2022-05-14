package com.fdel.service.auth.provider;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
	 */
	static public OAuth2UserInfo getOAuth2UserInfo(OAuth2UserRequest userRequest
			, OAuth2User oauth2User)
	{
		Provider provider = 
			Provider.ofString(userRequest.getClientRegistration().getRegistrationId()); 
		return provider.getOAuth2UserInfo(oauth2User);
	}
	
}
