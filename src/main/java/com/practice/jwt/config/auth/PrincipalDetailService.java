package com.practice.jwt.config.auth;

import com.practice.jwt.model.User;
import com.practice.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8081/login 요청시에 얘가 동작함
//Default 시큐리티의 로그인 요청이 /login 임
//하지만 시큐리티 설정에서 formLogin.disable() 이기때문에 동작 안함 그래서 동작하는 필터를 만들꺼임
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailService의 loadUserByUsername()");
        User userEntity = userRepository.findByUsername(username);
        System.out.println(userEntity +" 이게 userEntity" );

        return new PrincipalDetails(userEntity);
    }
}
