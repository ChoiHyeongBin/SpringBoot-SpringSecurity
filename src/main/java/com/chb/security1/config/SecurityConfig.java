package com.chb.security1.config;

import org.springframework.beans.factory.annotation.Autowired;

// 1. 코드받기(인증)
// 2. 엑세스토큰(권한)
// 3. 사용자프로필 정보를 가져오고 
// 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
// 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip 등급, 일반등급)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.chb.security1.config.oauth.PrincipalOauth2UserService;

@Configuration				// 메모리에 뜨게 함
@EnableWebSecurity		// 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)		// secured 어노테이션 활성화, preAuthorize/postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	// 해당 메서드의 리턴되는 오브젝트를 IoC 로 등록해줌
	@Bean		// Bean 으로 등록
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();		// csrf 비활성화
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()		// 이 주소로 들어오면 인증이 필요 (로그인한 사람만 들어올 수 있음), 인증만 되면 들어갈 수 있는 주소
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")		// 인증뿐만 아니라, 이 권한이 있는 사람만 들어가게 함
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")		// 'ROLE_ADMIN' 권한이 있어야 들어갈 수 있다
			.anyRequest().permitAll()		// 이외의 다른 요청은 허용이 됨 (로그인 안 해도 접속 가능)
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")		// /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해 줌
			.defaultSuccessUrl("/")				// 로그인 성공 시 index 페이지로 넘어감
			.and()
			.oauth2Login()
			.loginPage("/loginForm")		// 구글 로그인이 완료된 뒤의 후처리가 필요 Tip. 코드X, (엑세스토큰 + 사용자프로필정보 O)
			.userInfoEndpoint()
			.userService(principalOauth2UserService);
	}
	
}
