package com.hanium.fishing.utils;

import com.hanium.fishing.api.dto.response.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

//    private static final Long accessExpireTimeMs = 1000L * 60 * 60l; // 1시간 동안 토큰 유효
    // 1 달동안 토큰 유효
    private static final Long accessExpireTimeMs = 1000L * 60 * 60 * 24 * 30l; // 1달 동안 토큰 유효
    private static final Long refreshExpireTimeMs = 1000L * 60 * 60 * 24 * 60l; // 2달 동안 토큰 유효


    public static String getUserId(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId", String.class);
    }

    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
    public static TokenResponseDto createAllToken(String userId, String key) {

        return new TokenResponseDto(createToken(userId, "access", key), createToken(userId, "refresh", key));
    }

    public static String createToken(String userId, String type, String key) {

        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        long expireTimeMs = type.equals("access") ? accessExpireTimeMs : refreshExpireTimeMs;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
