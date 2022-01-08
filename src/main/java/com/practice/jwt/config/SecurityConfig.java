package com.practice.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration //IOC할 수 있게 만들어 줌
@EnableWebSecurity //이 config를 허용함
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //CorsConfig안에 있는 CorsFilter메소드를 @Bean등록을 해서 바로 빼올수 있음
    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) //@CrossOrigin(인증X) , 필터에 등록 인증(O)
                    .formLogin().disable()
                    .httpBasic().disable()
                .authorizeRequests()
                    .antMatchers("/api/v1/user").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/manager").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/admin").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();



    }
}
