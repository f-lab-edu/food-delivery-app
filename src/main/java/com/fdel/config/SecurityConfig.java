package com.fdel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.fdel.applicationservice.auth.PrincipalOauth2UserService;

/*
 * @EnableWebSecurity 스프링 시큐리티 필터가 스프링 필터 체인에 등록되게 함
 * @EnableGlobalMethodSecurity secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
 */
@Configuration
@EnableWebSecurity 
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

	/*
	 * OAuth 과정
	 * 1코드받기(인증), 2엑세스토큰(권한), 3사용자 프로필 정보를 가져와서, 4-1 그 정보를 토대로 회원가입을 자동으로 진행
	 * 4-2 필요하다면 사용자에게 추가 정보를 입력받아 저장(이메일, 전화 번호 등)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); 
		
		http.authorizeRequests()
				.antMatchers("/orderer/**").access("hasRole('ROLE_ORDERER')")
				.antMatchers("/restrantowner/**").access("hasRole('ROLE_RESTRANTOWNER')")
				.anyRequest().permitAll()
			.and() 
				.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다. 즉 컨트롤러에 /login을 안 만들어도 됩니다.
				.defaultSuccessUrl("/")
			.and()
				.oauth2Login()
				.loginPage("/loginForm")
				.userInfoEndpoint() //oauth2Login 성공 이후의 설정을 시작
				.userService(principalOauth2UserService); 
				/*
				 * 구글 로그인이 완료된 뒤의 후처리 필요 
				 * Tip 구글은 인증 코드를 가져오는 과정을 개발자가 다루지 않아도 
				 * 라이브러리가 (약세스 토큰+사용자프로필 정보)를 한번에 받아줍니다.
				 */
	}
	
}
