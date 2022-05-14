package com.fdel.service.auth.provider;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fdel.exception.authentication.ProviderException;
import com.fdel.exception.message.SimpleMessage;
import com.fdel.exception.message.authentication.ProviderMessage;

import lombok.Getter;

/**
 * Provider을 구분하기 위한 enum
 */
@Getter
public enum Provider{
	
	NONE("NONE", (oauth2User)->{throw new ProviderException(ProviderMessage.NOT_OAUTH2_PROVIDER.getMessage());}),
	GOOGLE("GOOGLE", (oauth2User)->new GoogleUserInfo(oauth2User.getAttributes())),
	FACEBOOK("FACEBOOK", (oauth2User)->new FaceBookUserInfo(oauth2User.getAttributes())),
	NAVER("NAVER", (oauth2User)->new NaverUserInfo((Map<String, Object>)oauth2User.getAttributes().get("response")));
	/* 
	 * 네이버 유저 정보는 response : {resultcode=00, message=success, 
	 * 		response={id= sadmsakda, email=someEmail@naver.com, name=홍길동}} 형태이다.
	 */
	
	private String provider;
	Function<OAuth2User, OAuth2UserInfo> oAuth2UserInfoMaker;
	
	Provider(String provider, Function<OAuth2User, OAuth2UserInfo> oAuth2UserInfoMaker){
		this.provider = provider;
		this.oAuth2UserInfoMaker = oAuth2UserInfoMaker;
	}
	
	public OAuth2UserInfo getOAuth2UserInfo(OAuth2User oAuth2User) {
		return oAuth2UserInfoMaker.apply(oAuth2User);
	}
	
	/**
	 * String 타입의 provider 정보를 받아 Provider 타입으로 바꿔준다.
	 * 
	 * @param provider string타입의 provider 정보 ex) NONE, GOOGLE, FACEBOOK, NAVER
	 * @return string 타입의 provider 정보와 대응하는 Provider 타입의 provider 정보 반환
	 * @throws IllegalArgumentException 일치하는 Provider 타입이 없는 경우 던져진다.
	 */
	public static Provider ofString(String stringProvider) {
		String upperCaseStringProvider = stringProvider.toUpperCase();
		return Arrays.stream(Provider.values())
			.filter(e -> e.getProvider().equals(upperCaseStringProvider))
			.findAny()
			.orElseThrow(()-> 
				new IllegalArgumentException(SimpleMessage.
						NO_MATCHING_TYPES_FOUND.getMessage()));
	}
}