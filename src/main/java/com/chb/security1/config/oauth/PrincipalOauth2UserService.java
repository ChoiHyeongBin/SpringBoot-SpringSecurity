package com.chb.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.chb.security1.config.auth.PrincipalDetails;
import com.chb.security1.model.User;
import com.chb.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	// 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
			System.out.println("PrincipalOauth2UserService getClientRegistration : " + userRequest.getClientRegistration());		// registrationId 로 어떤 OAuth 로 로그인 했는지 확인가능
			System.out.println("PrincipalOauth2UserService getAccessToken : " + userRequest.getAccessToken().getTokenValue());
			
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code 를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
		// userRequest -> loadUser 함수 호출 -> 구글로부터 회원프로필을 받아줌
			System.out.println("PrincipalOauth2UserService getAttributes : " + oauth2User.getAttributes());
			
		// 회원가입을 강제로 진행해볼 예정
		String provider = userRequest.getClientRegistration().getClientId();		// google
		String providerId = oauth2User.getAttribute("sub");								// google 의 id (sub 라는 이름)
		String username = provider + "_" + providerId;											// google_116522453331692469215
		String password = bCryptPasswordEncoder.encode("dummy");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
			
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			System.out.println("구글 로그인이 최초입니다.");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(providerId)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		} else {
			System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
	
}
