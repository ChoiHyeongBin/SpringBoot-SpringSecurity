package com.chb.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chb.security1.config.auth.PrincipalDetails;
import com.chb.security1.model.User;
import com.chb.security1.repository.UserRepository;

@Controller		// View 를 리턴
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(
			Authentication authentication, 
			@AuthenticationPrincipal PrincipalDetails userDetails) {		// DI (의존성 주입), @AuthenticationPrincipal -> session 정보에 접근할 수 있는 어노테이션
		System.out.println("/test/login ==================");
		PrincipalDetails principalDetatils = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("IndexController authentication : " + principalDetatils.getUser());
		
		System.out.println("IndexController userDetatils : " + userDetails.getUser());		
		return "세선 정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {		// DI (의존성 주입), @AuthenticationPrincipal -> session 정보에 접근할 수 있는 어노테이션
		System.out.println("/test/oauth/login ==================");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();		// OAuth2User 로 다운캐스팅
		System.out.println("IndexController authentication : " + oauth2User.getAttributes());		// 동일한 결과 (1번째 방법)
		System.out.println("IndexController oauth2User : " + oauth.getAttributes());						// 동일한 결과 (2번째 방법)

		return "OAuth 세선 정보 확인하기";
	}

	@GetMapping({"", "/"})	// 공백과 / 를 주소로 받음
	public String index() {
		// 머스테치를 사용해 볼 것임 -> 기본폴더는 src/main/resources/
		// ViewResolver 설정 : templates (prefix), .mustache (suffix) (생략가능)
		return "index";	// src/main/resources/templates/index.mustache
	}
	
	// OAuth 로그인을 해도 PrincipalDetails,
	// 일반 로그인을 해도 PrincipalDetails 타입으로 받을 수 있음
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("IndexController principalDetails : " + principalDetails.getUser());
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
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
			System.out.println("IndexController user : " + user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user);		// 회원가입이 잘 됨. but, 비밀번호 : 1234 => 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화가 안되었기 때문
			
		return "redirect:/loginForm";			// 함수 재사용이 가능, Model 데이터를 가지고 올 수 있다
	}
	
	@Secured("ROLE_ADMIN")		// 하나를 걸때 사용
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
}
