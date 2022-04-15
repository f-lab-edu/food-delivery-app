package com.fdel.applicationservice.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdel.entity.User;
import com.fdel.repository.UserRepository;



@Service
public class PrincipalDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	/*
	 * 여기서 중요한 점은 loginForm 페이지에서
	 * 사용자 계정 form input 이름을 username이라고 
	 * 똑같이 하지 않으면 이 파라미터로 받아지지 않는다.
	 * 
	 * SecurityContextHolder(SecurityContext(Authentication(UserDetatils)))
	 * loadUserByUsername()에서 UserDetails가 return될 때 Authentication 객체 안에 위치하게 된다.
	 * 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}

}

