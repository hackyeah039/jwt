package com.practice.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


        //토큰을 만들어서 토큰 이름이 '코스' 코스라는 토큰이 넘어오면 인증되고 아니면 더이상 필터 못하게 해서 컨트롤러로 진입 못하게

        //이제 토큰 :cos 이걸 만들어 줘야 한다. req에서 id,pw가 정상적으로 들어왔을 때(맨처음) 로그인이 완료 되면 토큰을 만들어주고 그걸 응답을 해 줌.
        //그리고 로그인 후에 요청할 때 마다 header에 Authorization 의 밸류값으로 토큰을 가져오면
        //이 토큰이 내가 만든게 맞는지 안맞는지 확인만 하면 됨.(만드는 방식은 HS256, RSA 방식들이 있음)

       if(req.getMethod().equals("POST")){
            System.out.println("POST요청됨");

            String headerAuth=req.getHeader("Authorization");

            System.out.println(headerAuth);

            if(headerAuth.equals("cos")){   // req Header에서 Authorization의값이 "cos"인것만 가져와서 인증
                chain.doFilter(req, res);
            }else { //아닌건 인증 ㄴㄴ
                System.out.println("필터 3");
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("인증안됨");
            }
        }
        //체인을 안적고
        // PrintWriter out = response.getWriter(); ,,, out.print("안녕");
        // 이렇게 적으면 필터 타는순간 앱이 그냥 끝남 , 안끝나고 계속 프로세스를 진행하려면 chain을 !
    }
}
