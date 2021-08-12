package com.jeeneee.realworld.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    private static final String HEADER_PREFIX = "Token ";

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.validity-in-minutes}")
    private Integer validity;

    public String generate(String email) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(
                Date.from(
                    currentTime.plusMinutes(validity).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
    }

    public Optional<String> extract(String header) {
        if (header == null) {
            return Optional.empty();
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return Optional.of(header.substring(HEADER_PREFIX.length()));
    }

    public String parseSubject(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid Json Web Token");
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Expired Json Web Token");
        }
    }
}
