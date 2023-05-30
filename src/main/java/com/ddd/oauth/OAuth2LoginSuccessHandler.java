    package com.ddd.oauth;

    import com.ddd.jwt.TokenService;
    import com.ddd.user.domain.Role;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import lombok.AllArgsConstructor;
    import lombok.NoArgsConstructor;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.stereotype.Component;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        private final TokenService tokenService;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            log.info("OAuth2 Login 성공!");
            try {
                CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

                // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
                if(oAuth2User.getRole() == Role.GUEST) {
                    String accessToken = tokenService.createAccessToken(oAuth2User.getEmail());
                    response.addHeader(tokenService.getAccessHeader(), "Bearer " + accessToken);
                    response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

                    tokenService.sendAccessAndRefreshToken(response, accessToken, null);
                } else {
                    loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
                }
            } catch (Exception e) {
                throw e;
            }

        }

        private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
            String accessToken = tokenService.createAccessToken(oAuth2User.getEmail());
            String refreshToken = tokenService.createRefreshToken();
            response.addHeader(tokenService.getAccessHeader(), "Bearer " + accessToken);
            response.addHeader(tokenService.getRefreshHeader(), "Bearer " + refreshToken);

            tokenService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
            tokenService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
        }
    }