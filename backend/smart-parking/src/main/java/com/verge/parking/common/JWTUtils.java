package com.verge.parking.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {
    public static final String header = "Authorization";

    public static final String tokenPrefix = "Bearer";

    public static final String secret = "Pgj0w3w1S5WZxy00";

    public static final long expireTime = 1000*60*120;

    public static String createToken(Integer userId){
        return tokenPrefix + JWT.create()
                .withSubject("UserInfo")
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(secret));
    }

    public static LoginInfo validateToken(String token){
        try {
            Claim userId = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getClaim("userId");
            if (userId != null) {
                return new LoginInfo(userId.asInt());
            } else {
                throw new RuntimeException("没有查询到用户信息");
            }
        } catch (TokenExpiredException e){
            throw new RuntimeException("token已经过期");
        } catch (Exception e){
            throw new RuntimeException("token验证失败");
        }
    }
}

