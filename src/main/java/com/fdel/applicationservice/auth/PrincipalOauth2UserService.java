package com.fdel.applicationservice.auth;

import static com.fdel.applicationservice.auth.provider.OAuth2UserInfoFactory.getOAuth2UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fdel.applicationservice.UserApplicationService;
import com.fdel.applicationservice.auth.provider.OAuth2UserInfo;
import com.fdel.applicationservice.auth.provider.Provider;
import com.fdel.entity.User;
import com.fdel.entity.User.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	private final PasswordEncoder passwordEncoder;
	private final UserApplicationService userApplicationService;
	
	/*
	 * 인증 처리후 구글로 부터 받은 userRequst 데이터에 대한 후처리 함수
	 * 
	 * OAuth2-Client 라이브러리는 구글과 페이스북, 트위터 프로바이더에 한해
	 * 코드를 받아 이증받고 토큰을 받아오는 과정을 생략하고 개발자가 곧바로
	 * 사용자 정보를 처리할 수 있도록 해준다.
	 * 
	 * 원래 OAuth2 인증 과정은 아래와 같다.
	 * 사용자가 포털 사이트에서 로그인 -> redirect uri로 코드 획득(인증)
	 * -> 획득한 코드로 토큰 요청 -> 토큰(권한)을 받아 다시 사용자 정보 요청
	 * -> 응답받은 사용자 정보를 키값으로 등록된 세션 정보가 있는지 확인
	 * -> 없다면 repository로부터 DB에 등록된 회원인지 확인
	 * -> 회원가입 되어 있지 않다면 회원가입 처리
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) 
			throws OAuth2AuthenticationException {
		
//		log.info("==============OAuth 인증정보==============");
//		log.info("getClientRegistration : {}", userRequest.getClientRegistration()); //registerationId로 어떤 Oauth로 로그인했는지 확인 가능
//		log.info("getAccessToken : {}", userRequest.getAccessToken());
		
		OAuth2User oauth2User = super.loadUser(userRequest);

//		log.info("getAttributes : {}", oauth2User.getAttributes());
		
		OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(userRequest, oauth2User);
		
		//회원가입을 강제로 진행
		Provider provider = oAuth2UserInfo.getProvider(); //google, faceBook
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId; //gooogle_109742856324325325
		String password = passwordEncoder.encode("OAuth계정공통비번");
		String email = oAuth2UserInfo.getEmail();
		List<Role> roles = new ArrayList<>(Arrays.asList(Role.ORDERER));
		
		User userEntity = userApplicationService.findByUserName(username);
		
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.provider(provider)
					.providerId(providerId)
					.roles(roles)
					.build();
			
			userApplicationService.regist(userEntity);
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}

	
}