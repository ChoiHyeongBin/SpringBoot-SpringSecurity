package com.chb.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller		// View 를 리턴
public class IndexController {

	@GetMapping({"", "/"})	// 공백과 / 를 주소로 받음
	public String index() {
		// 머스테치를 사용해 볼 것임 -> 기본폴더는 src/main/resources/
		// ViewResolver 설정 : templates (prefix), .mustache (suffix) (생략가능)
		return "index";	// src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	// 스프링시큐리티가 해당 주소를 낚아채서 body 의 login 을 리턴하지 못함 -> SecurityConfig 파일 생성 후 작동 안함 (중간에 가로채지 않음)
	@GetMapping("/login")
	public @ResponseBody String login() {
		return "login";
	}
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨";
	}
	
}
