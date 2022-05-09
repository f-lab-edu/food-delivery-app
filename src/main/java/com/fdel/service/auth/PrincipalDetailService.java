package com.fdel.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdel.entity.User;
import com.fdel.exception.domain.user.UserNotFoundException;
import com.fdel.exception.message.UserMessage;
import com.fdel.repository.UserRepository;


/**
 * 어플리케이션 로그인시 입력받은 값으로 인증 서비스를 제공한다.
 */
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
	/**
	 * 로그인한 유저 정보를 찾아서 반환한다.
	 * 반환된 유저 정보는 Security Context의 Authentication 객체에 저장된다.
	 * 
	 * @param username 로그인시 사용자가 입력한 username 정보를 입력받는다.
	 * @return UserDetails를 구현한 PrincipalDetails에 유저 정보를 담아 반환한다.
	 * @throws UsernameNotFoundException 일치하는 유저를 찾을 수 없으면 던져진다.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		
		User userEntity = userRepository
				.findByUsername(username)
				.orElseThrow(
						()->new UserNotFoundException(
							UserMessage.USER_NOT_FOUND_MATCHING_THE_USERNAME.getMessage()));
		
		return new PrincipalDetails(userEntity);
	}

}

