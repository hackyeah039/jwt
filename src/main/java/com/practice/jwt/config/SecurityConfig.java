package com.practice.jwt.config;

import com.practice.jwt.config.jwt.JwtAuthenticationFilter;
import com.practice.jwt.filter.MyFilter1;
import com.practice.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import javax.persistence.Basic;

@Configuration //IOC할 수 있게 만들어 줌
@EnableWebSecurity //이 config를 허용함
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //CorsConfig안에 있는 CorsFilter메소드를 @Bean등록을 해서 바로 빼올수 있음
    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //스프링 필터체인에 어떤게 있는지 알아야 됨
        //이건 BasicAuthenticaionFilter전에 이  Myfilter를 작동한다는 뜻
        //http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
        //근데 Security에 걸 필요는 없고,
        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        //시큐리티 필터중 가장 빨리 시작되는 필터보다도 전에 시작해서 MyFilter가 가장 빨리 시작한다.
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 쓰지 않겠다.
                .and()
                .addFilter(corsFilter) //@CrossOrigin(인증X) , 필터에 등록 인증(O)
                    .formLogin().disable()
                    .httpBasic().disable()
                    .addFilter(new JwtAuthenticationFilter(authenticationManager()))// AuthenticationManager 이 파라미터를 꼭 넘겨줘야 함
                .authorizeRequests()
                    .antMatchers("/api/v1/user").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/manager").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();



    }
}
