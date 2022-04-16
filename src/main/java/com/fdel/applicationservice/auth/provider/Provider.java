package com.fdel.applicationservice.auth.provider;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Provider{
	
	NONE("NONE"),
	GOOGLE("GOOGLE"),
	FACEBOOK("FACEBOOK"),
	NAVER("NAVER");
	
	private String provider;
	
	Provider(String provider){
		this.provider = provider;
	}
	
	public static Provider ofString(String provider) {
		return Arrays.stream(Provider.values())
			.filter(e -> e.getProvider().equals(provider))
			.findAny()
			.orElseThrow(()-> new IllegalArgumentException("일치하는 OAuth 타입이 존재하지 않습니다."));
	}
}