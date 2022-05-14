package com.fdel.service.auth.provider;

/**
 * OAutho2로 인증된 사용자 정보의 인터페이스를 정의한다.
 */
public interface OAuth2UserInfo {
	String getProviderId();
	Provider getProvider();
	String getEmail();
	String getName();
}
