package com.fdel.service.auth.provider;

import java.util.Map;

/**
 * OAutho2로 인증된 Google 사용자 정보를 담는다.
 */
public class GoogleUserInfo implements OAuth2UserInfo{

	private Map<String, Object> attributes; //oauth2User.getAttributes()
	
	public GoogleUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return (String) attributes.get("sub");
	}

	@Override
	public Provider getProvider() {
		return Provider.GOOGLE;
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}
	
}
