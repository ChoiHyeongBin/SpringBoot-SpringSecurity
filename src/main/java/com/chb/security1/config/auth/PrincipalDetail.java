package com.chb.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chb.security1.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다
// 로그인 진행이 완료가 되면 시큐리티 session 을 만들어 준다. (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨
// User 오브젝트타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails (PrincipalDetail)

public class PrincipalDetail implements UserDetails {

	private User user;		// 콤포지션

	public PrincipalDetail(User user) {
		this.user = user;
	}

	// 해당 User 의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();	// ArrayList 는 Collection 의 자식
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료됬는지
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겼는지
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 너무 오래 사용되지는 않았는지
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화되어 있는지
	@Override
	public boolean isEnabled() {
		// 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로 한다.
		// 현재시간 - 로그인시간 => 1년을 초과하면 return false;
		return true;
	}

}
