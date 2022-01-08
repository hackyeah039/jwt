package com.practice.jwt.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 1");
        chain.doFilter(request, response);
        //체인을 안적고
        // PrintWriter out = response.getWriter(); ,,, out.print("안녕");
        // 이렇게 적으면 필터 타는순간 앱이 그냥 끝남 , 안끝나고 계속 프로세스를 진행하려면 chain을 !
    }
}
