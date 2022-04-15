package com.fdel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdel.applicationservice.ApplicationLoginService;
import com.fdel.controller.dto.JoinDto;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final ApplicationLoginService applicationLoginService;
	
	@GetMapping({"","/"})
	public String index() {
		return "/index";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(JoinDto joinDto) {
		applicationLoginService.registByJoinDto(joinDto);
		return "redirect:/loginForm";
	}
}