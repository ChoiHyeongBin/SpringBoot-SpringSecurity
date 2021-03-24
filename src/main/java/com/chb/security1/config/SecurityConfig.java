package com.chb.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration				// 메모리에 뜨게 함
@EnableWebSecurity		// 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();		// csrf 비활성화
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()		// 이 주소로 들어오면 인증이 필요 (로그인한 사람만 들어올 수 있음)
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")		// 인증뿐만 아니라, 이 권한이 있는 사람만 들어가게 함
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")		// 'ROLE_ADMIN' 권한이 있어야 들어갈 수 있다
			.anyRequest().permitAll()		// 이외의 다른 요청은 허용이 됨 (로그인 안 해도 접속 가능)
			.and()
			.formLogin()
			.loginPage("/login");
	}
	
}
