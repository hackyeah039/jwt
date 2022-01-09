package com.practice.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.jwt.config.auth.PrincipalDetails;
import com.practice.jwt.model.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

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
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중 ");
        // request 의 바이트 값을 sout해서 확인 해 보려고 readLine() 호출
//        BufferedReader br =request.getReader();
//        String input = null;
//
//        while((input=br.readLine()) !=null) {
//            System.out.println(input);
//        }

        //위의 방식은 버퍼드 리더 직접 해서 어떻게 req값이 들어오는지 input값을 readLine()으로 읽은 거고,
        //밑의 방식은 ObjectMapper가 데이터를 User에 직접 매핑까지 편리하게 간단하게 한거고,이걸 sout으로 확인

        ObjectMapper om = new ObjectMapper();
        User user = om.readValue(request.getInputStream(), User.class);
        System.out.println(user);
        //req에 담긴 User 객체 내용을 베이스로 토큰을 만듦
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        System.out.println(authenticationToken +" 토큰 생성" + authenticationToken.getPrincipal().toString());
        //토큰을 PrincipalDetailsService 의 LoadByUsername() 가 자동 실행됨
        //여기서 loadByUsername()에서 user판별이 제대로 되면 PrincipalDetails(userEntity로 반환 되는데 )
        //그걸 authentication으로 리턴
        System.out.println("여기서 부터 들어가나?");
        Authentication authentication=
                authenticationManager.authenticate(authenticationToken);
        System.out.println("여긴가?");
        System.out.println(authentication +"authentication 생성");
        // authentication 객체가 session 영역에 저장됨 => 로그인이 되었다.
        PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();
        System.out.println(principalDetails.getUser().getUsername());
        System.out.println(request.getInputStream().toString() +"toString()");
        System.out.println("===============================================");
        // 1. 여기선 req의 username, password를 받아서
        // 2. 정상인지 로그인 시도를 해 봄. authenticationManager가 로그인 시도를 하면
        //      PrincipalDetailService 가 호출 -> loadUserByUsername() 가 자동실행
        //      -> PrincipalDetails 생성 -> session에 User객체 저장 (for What -> 권한 관리를 위해서(페이지 권한))

        // 3. 이제 클라이언트한테 res할 때 JWT토큰을 만들어서 응답해주면 됨.

        //authentication객체 저장 하는건 세션에 저장해서 권한 관리를 security가 해주니깐 편해서 하는거임.
        //굳이 JWT에서 session  만들필요는 없음. 세션 권한 처리때문에 하는거임.
        //

        return authentication;
    }

    // attemptAuthentication에서 인증 완료되면 successfulAuthentication 함수가 실행됨
    // 여기서 JWT 토큰을 만들어서 req header보내면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 즉 인증이 완료되었다는 뜻임");

        PrincipalDetails principalDetails =(PrincipalDetails) authResult.getPrincipal();
        //RSA 방식은 아니고 Hash 암호방식?
        String jwtToken = JWT.create()
                .withSubject("cos토큰")  //크게 의미 없음
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10))) //만료시간 10분
                .withClaim("id",principalDetails.getUser().getId())
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization" , "Bearer "+jwtToken);
        //이제 response header 에 Authorization키 , 밸류값으로 토큰을 반환함
        //그럼 이제 서버에서 토큰이 req때마다 유효한지 체크하는 필터를 만들어야 됨
    }
}
