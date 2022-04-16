package com.fdel.applicationservice.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fdel.entity.User;

import lombok.Data;


/**
 * 어플리케이션으로 로그인할 경우 유저 정보를 저장하는 UserDetails와
 * OAuth2 방식으로 로그인할 경우 유저 정보를 저장하는 OAuth2User를
 * 감싸서 공통된 인터페이스를 제공하기 위한 클래스다.
 */
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private static final long serialVersionUID = 1L;
	private User user;
	private Map<String, Object> attributes;
	
	//일반 로그인용 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth 로그인용 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	/**
	 *  해당 유저의 권한들을 Collection 형태로 반환하는 함수다. 
	 *  @Secured나 @PreAuthorize를 만나서 권한을 체크할 때 호출된다.
	 *  
	 *  @return Collection<? extends GrantedAuthority> 타입의 권한 컬렉션을 반환한다.
	 *  		컬렉션 요소인 GrantedAuthority는 String 타입의 권한 정보를 반환하는
	 *  		getAuthority() 함수를 가진다.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorityCollecttion = new ArrayList<>();
		user.getRoles().forEach(
					e-> collectGrantedAuthority(grantedAuthorityCollecttion, e.getRole()));
		return grantedAuthorityCollecttion;
	}

	private void collectGrantedAuthority(Collection<GrantedAuthority> collect, String e) {
		collect.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getAuthority() {
				return e;
			}		
		});
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}

}