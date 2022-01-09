package com.practice.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청해서 username, password를 post로 전송하면,
// UsernamePasswordAuthenticationFilter이게 동작을 함

// 지금은 formLogin.disable()때문에 동작을 안하는데 이걸 다시 등록을 하면 동작을 함.
// How? => .addFilter에서 JwtAuthenticationFilter(UsernamePasswordAuthenticationFilter이걸 동작시킨 걸) 추가시키면 됨

// UsernamePasswordAuthenticationFilter이게 AuthenticationManager 이걸 통해서 로그인을 진행하기 때문에 이걸 만들어줌
// SecurityConfig 에서 WebSecurityConfigurerAdapter 이게 AuthenticationManager이걸 들고 있어서 거기서 바로 넣어줄 수 있음

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 얘가 로그인 시도를 위해서 실행되는 함수임
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중 ");

        // 1. 여기선 req의 username, password를 받아서
        // 2. 정상인지 로그인 시도를 해 봄. authenticationManager가 로그인 시도를 하면
        //      PrincipalDetailService 가 호출 -> loadUserByUsername() 가 자동실행
        //      -> PrincipalDetails 생성 -> session에 User객체 저장 (for What -> 권한 관리를 위해서(페이지 권한))

        // 3. 이제 클라이언트한테 res할 때 JWT토큰을 만들어서 응답해주면 됨.
        return super.attemptAuthentication(request, response);
    }
}
