package com.fdel.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdel.dto.user.UserDto;
import com.fdel.entity.User;

/**
 * 유저와 관련된 어플리케이션 서비스 인터페이스다.
 */
@Transactional
@Service
public interface UserService {
	Optional<User> findByUserName(String userName);
	/**
	 * 가입 form으로부터 받아온 dto객체를 받아서 유저를 어플리케이션에 등록한다.
	 * 
	 * @param userDto form으로부터 받아온 정보다.
	 */

  void regist(UserDto userDto);
	void regist(User user);

}
