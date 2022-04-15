package com.fdel.repository.converter;

import javax.persistence.AttributeConverter;

import com.fdel.applicationservice.auth.provider.Provider;

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
