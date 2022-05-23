package com.fdel.service.auth.provider;

import java.util.Map;

/**
 * OAutho2로 인증된 FaceBook 사용자 정보를 담는다. 
 */
public class FaceBookUserInfo implements OAuth2UserInfo{

	private final Map<String, Object> attributes; //oauth2User.getAttributes()
	
	public FaceBookUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return (String) attributes.get("id");
	}

	@Override
	public Provider getProvider() {
		return Provider.FACEBOOK;
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
