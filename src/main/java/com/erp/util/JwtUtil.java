package com.erp.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails, int empId) {
        SecretKey key = (SecretKey) getSignKey();

        return Jwts.builder()
                // .subject(empCode)
                .subject(userDetails.getUsername()) // empCode
                .claim("empId", empId)
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))

                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(key)
                .compact();

    }

    // extract the empCode from the token
    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    // extract the empId from the token
    public int extractEmpId(String token) {
        Claims claims = parseToken(token);
        Object empIdObj = claims.get("empId");
        if (empIdObj instanceof Integer) {
            return (Integer) empIdObj;
        } else if (empIdObj instanceof Number) {
            return ((Number) empIdObj).intValue();
        } else {
            throw new RuntimeException("Invalid empId in token.");
        }

    }

    // etract empId directly from request
    public int extractEmpIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = header.substring(7);
        return extractEmpId(token);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // extract authorities
    public List<String> extractAuthorities(String token) {
        Claims claims = parseToken(token);
        return claims.get("authorities", List.class);
    }

}
