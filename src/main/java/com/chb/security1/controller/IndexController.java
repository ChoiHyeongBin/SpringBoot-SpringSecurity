package com.chb.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller		// View 를 리턴
public class IndexController {

	@GetMapping({"", "/"})	// 공백과 / 를 주소로 받음
	public String index() {
		// 머스테치를 사용해 볼 것임 -> 기본폴더는 src/main/resources/
		// ViewResolver 설정 : templates (prefix), .mustache (suffix) (생략가능)
		return "index";	// src/main/resources/templates/index.mustache
	}
	
}
