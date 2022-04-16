package com.fdel.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.fdel.applicationservice.auth.provider.Provider;
import com.fdel.exception.message.SimpleMessage;
import com.fdel.exception.message.UserMessage;
import com.fdel.repository.converter.UserOAuthConverter;
import com.fdel.repository.converter.UserRolesConverter;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 Entity 클래스입니다.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
public class User {
	
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
	
	@CreationTimestamp //이 애너테이션은 setter로 주입하지 않아도 자동 생성되게 해준다.
	private Timestamp createDate;
	
	@Getter
	public static enum Role {
		ORDERER("ROLE_ORDERER"),
		RESTRANTOWNER("ROLE_RESTRANTOWNER");
		
		private String role;
		
		Role(String role){
			this.role = role;
		}
		
		public static Role ofString(String stringRole) {
			return Arrays.stream(Role.values())
				.filter(e -> e.getRole().equals(stringRole))
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
	public void validateIntegrity() {
		if(StringUtils.isBlank(username)
				||StringUtils.isBlank(password)
				||StringUtils.isBlank(email)
				||roles.isEmpty()
				||provider == null) {
			throw new IllegalStateException(UserMessage
						.INTEGRITY_OF_THE_USER_HAS_BEEN_VIOLATED.getMessage());
		}
	}
	
	public User(Integer id, String username, String password, 
			String email, List<Role> roles, Provider provider,
			String providerId, Timestamp createDate) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
	}
	
}