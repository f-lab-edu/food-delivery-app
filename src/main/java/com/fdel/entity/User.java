package com.fdel.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.fdel.exception.message.SimpleMessage;
import com.fdel.exception.message.UserMessage;
import com.fdel.repository.converter.UserOAuthConverter;
import com.fdel.repository.converter.UserRolesConverter;
import com.fdel.service.auth.provider.Provider;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 Entity 클래스입니다.
 */
@Entity
@Builder
@NoArgsConstructor
public class User extends BaseTimeEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String email;
	
	@Convert(converter = UserRolesConverter.class)
	@Builder.Default
	private List<Role> roles = new ArrayList<>();
	
	//OAuth를 제공하는 사이트
	@Convert(converter = UserOAuthConverter.class)
	private Provider provider;
	//OAuth 제공 사이트의 유저 계정 id
	@Builder.Default
	private String providerId = "none";
	
	@Getter
	public static enum Role {
		ORDERER("ROLE_ORDERER"),
		STORE_OWNER("ROLE_STORE_OWNER");
		
		private final String role;
		
		Role(String role){
			this.role = role;
		}
		
		//대소문자 상관없이, 중간 _ 문자 상관없이, Prefix ROLE 상관없이 string 타입 role과 일치하는 Role을 반환한다. 
		public static Role ofString(String stringRole) {
			String upperCaseStringRole = stringRole.toUpperCase().replace("_","").replace("ROLE","");
			return Arrays.stream(Role.values())
				.filter(e -> e.getRole().replace("_","").replace("ROLE","").equals(upperCaseStringRole))
				.findAny()
				.orElseThrow(()-> 
					new IllegalArgumentException(SimpleMessage
							.NO_MATCHING_TYPES_FOUND.getMessage()));
		}
		
		public static List<Role> StringListToRoleList(List<String> stringRoleList){
			return stringRoleList.stream().map(e->ofString(e)).toList();
		}
	}
	
	/**
	 * 스스로 각 필드의 무결성을 검증합니다.
	 * DB에 저장되기 전에 호출됩니다.
	 */
	private void validateIntegrity() {
		if(StringUtils.isBlank(username)
				||StringUtils.isBlank(password)
				||StringUtils.isBlank(email)
				||roles.isEmpty()
				||provider == null) {
			throw new IllegalStateException(UserMessage
						.INTEGRITY_OF_THE_USER_HAS_BEEN_VIOLATED.getMessage());
		}
	}
	
	public User(String username, String password, String email, List<Role> roles, Provider provider, String providerId) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.provider = provider;
		this.providerId = providerId;
	}
	
	public User(Integer id, String username, String password, 
			String email, List<Role> roles, Provider provider,
			String providerId) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.provider = provider;
		this.providerId = providerId;
	}
	
	/**
	 * user 객체가 영속화되기 전에 필요한 정보를 초기화하고
	 * 무결성 검사를 합니다.
	 * 
	 * @param provider 인증을 제공해준 프로바이더
	 */
	public void init(Provider provider) {
		this.provider = provider;
		validateIntegrity();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public List<Role> getRoles() {
		return roles;
	}
	
}