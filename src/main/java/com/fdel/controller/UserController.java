package com.fdel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fdel.dto.user.UserDto;
import com.fdel.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 유저의 로그인 및 회원가입 요청을 처리하는 컨트롤러입니다.
 */
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userApplicationService;
	
	@GetMapping("/loginform")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinform")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(UserDto userDto) {
		userApplicationService.regist(userDto);
		return "redirect:/users/loginform";
	}
}