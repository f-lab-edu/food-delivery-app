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
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) 
			throws OAuth2AuthenticationException {
		
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