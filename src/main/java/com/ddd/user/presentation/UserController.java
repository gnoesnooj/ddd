package com.ddd.user.presentation;

import com.ddd.jwt.TokenService;
import com.ddd.oauth.CustomOAuth2UserService;
import com.ddd.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @PostMapping("/sign-up")
    public String signUp() throws Exception {
        return "회원가입 성공";
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}