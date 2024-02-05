package pl.marcinzygmunt.jwt.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import pl.marcinzygmunt.jwt.JwtConfigurationProperties;
import pl.marcinzygmunt.jwt.model.exception.JwtValidationException;
import pl.marcinzygmunt.jwt.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

   private final JwtConfigurationProperties jwtConfigurationProperties;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtConfigurationProperties.getCookieName());
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(jwtConfigurationProperties.getCookieName(), jwt)
                .path(jwtConfigurationProperties.getCookiePath())
                .maxAge(TimeUnit.MINUTES.toSeconds(jwtConfigurationProperties.getExpirationMin()))
                .httpOnly(jwtConfigurationProperties.isCookieHttp())
                .secure(jwtConfigurationProperties.isCookieSecure())
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtConfigurationProperties.getCookieName(), null).path("/api").build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigurationProperties.getSecret()));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
           throw new JwtValidationException(e.getMessage());
        }
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(jwtConfigurationProperties.getExpirationMin())))
                .signWith(key())
                .compact();
    }
}
