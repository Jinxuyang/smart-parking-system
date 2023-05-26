package com.verge.parking.filter;

import com.verge.parking.common.JWTUtils;
import com.verge.parking.common.LoginInfo;
import com.verge.parking.common.UserContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@WebFilter
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        if (matcher.match("**/login", uri)) {
            LoginInfo loginInfo = UserContextHolder.getLoginInfo();
            if (loginInfo != null) {
                String token = JWTUtils.createToken(loginInfo.getUserId());
                resp.addCookie(new Cookie("token", token));
            }
        } else {
            String token = req.getHeader("Authentication");
            if (token != null && token.startsWith(JWTUtils.tokenPrefix)) {
                LoginInfo loginInfo = JWTUtils.validateToken(token);
                UserContextHolder.setUserInfo(loginInfo);
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        chain.doFilter(request, response);
    }
}
