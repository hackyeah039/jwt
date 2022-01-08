package com.practice.jwt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        System.out.println("하하하 시작");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //서버 응답시 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는것.
        //자바스크립트 AJAX,PATCH,Axios 라이브러리를 요청할때 자바스크립트를 내 서버가 받을수 있게 만든느 것
        config.addAllowedHeader("*");//모든 header에 응답을 허용하겠다.
        config.addAllowedMethod("*");//모든 post,get,put,delete,patch 메소드 허용을 하겠다.
        config.addAllowedOrigin("*");//모든 ip에 응답을 허용하겠다.
        source.registerCorsConfiguration("/api/**",config); //여기로 들어오는 건 config 설정 !
        System.out.println("하하하끝");
        return new CorsFilter(source);
        //이걸 필터에 등록을 해야 됨.
    }
}
