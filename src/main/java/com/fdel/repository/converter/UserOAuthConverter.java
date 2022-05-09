package com.fdel.repository.converter;

import javax.persistence.AttributeConverter;

import com.fdel.service.auth.provider.Provider;

/**
 * User Entity의 provider 부분을 어플리케이션 type(Provider)와
 * 데이터베이스 type(String)간의 변환을 해줍니다. 
 */
public class UserOAuthConverter 
		implements AttributeConverter<Provider, String> {

	@Override
	public String convertToDatabaseColumn(Provider attribute) {
		return attribute.getProvider();
	}

	@Override
	public Provider convertToEntityAttribute(String dbData) {
		return Provider.ofString(dbData);
	}

}
