package com.fdel.repository.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.fdel.entity.User;
import com.fdel.entity.User.Role;

/**
 * User Entity의 roles 부분을 어플리케이션 type({@literal List<Role>})와
 * 데이터베이스 type(String)간의 변환을 해줍니다. 
 */
public class UserRolesConverter 
		implements AttributeConverter<List<User.Role>, String>{

	@Override
	public String convertToDatabaseColumn(List<Role> attribute) {
		List<String> StringRoleList = attribute.stream()
			.map(e->e.getRole()).sorted().toList();
		
		return String.join(",", StringRoleList);
	}

	@Override
	public List<Role> convertToEntityAttribute(String dbData) {
		if(dbData.length() > 0){
            return Arrays.asList(dbData.split(",")).stream()
            		.map(e->Role.ofString(e)).toList();
        }
		return new ArrayList<Role>();
	}

}
