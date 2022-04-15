package com.fdel.applicationservice.auth.provider;


public interface OAuth2UserInfo {
	String getProviderId();
	Provider getProvider();
	String getEmail();
	String getName();
}
