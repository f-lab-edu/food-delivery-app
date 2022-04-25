package com.fdel.controller.authmockconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithAnonymousUser;

import com.fdel.entity.User.Role;

/**
 * Anonymous 권한을 가진 유저가 들어온 경우를 테스트하고 싶을 때
 * test 함수 또는 test 클래스 레벨에 사용할 수 있습니다.
 * 
 * 이 애너테이션을 사용하면 mockMvc에서 사용되어지는 
 * Authentication 객체에 Anonymous 유저의 정보를 가진
 * principal 객체가 할당됩니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithAnonymousUser
public @interface WithMockAnonymous {

}
