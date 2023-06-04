package com.verge.parking.filter;

import com.verge.parking.common.JWTUtils;
import com.verge.parking.common.LoginInfo;
import com.verge.parking.common.UserContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Objects;

@WebFilter
@Component
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        if (!matcher.match("/smart_parking/user/login", uri) && !Objects.equals(req.getMethod(), "OPTIONS")) {
            String token = req.getHeader("Authorization");
            if (token != null && token.startsWith(JWTUtils.tokenPrefix)) {
                LoginInfo loginInfo = JWTUtils.validateToken(token);
                if (loginInfo != null) {
                    UserContextHolder.setUserInfo(loginInfo);
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        chain.doFilter(request, response);
    }
}
