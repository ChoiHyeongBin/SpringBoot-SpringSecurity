package com.chb.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chb.security1.model.User;

// CRUD 함수를 JpaRepository 가 들고 있음
// @Repository 라는 어노테이션이 없어도 IoC 가 됨 -> JpaRepository 를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {		// User 클래스의 PK 가 int 이므로 Integer 로 선언

}
