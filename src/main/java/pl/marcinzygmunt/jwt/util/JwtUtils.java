package pl.marcinzygmunt.jwt.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import pl.marcinzygmunt.jwt.JwtConfigurationProperties;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;
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
        return getCookieValueByName(request,jwtConfigurationProperties.getJwtCookieName());
    }
    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtConfigurationProperties.getRefreshCookieName());
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return generateCookie(jwtConfigurationProperties.getJwtCookieName(),
                jwt,
                jwtConfigurationProperties.getJwtCookiePath(),
                jwtConfigurationProperties.getExpirationMin());
    }

    public ResponseCookie generateJwtCookie(UserAccountEntity userAccountEntity) {
        String jwt = generateTokenFromUsername(userAccountEntity.getEmail());
        return generateCookie(jwtConfigurationProperties.getJwtCookieName(),
                jwt,
                jwtConfigurationProperties.getJwtCookiePath(),
                jwtConfigurationProperties.getExpirationMin());
    }


    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtConfigurationProperties.getRefreshCookieName(),
                refreshToken,
                jwtConfigurationProperties.getRefreshCookiePath(),
                jwtConfigurationProperties.getRefreshExpirationMin());
    }

    private ResponseCookie generateCookie(String name, String value, String path, int expirationMin) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(TimeUnit.MINUTES.toSeconds(expirationMin))
                .httpOnly(jwtConfigurationProperties.isCookieHttp())
                .secure(jwtConfigurationProperties.isCookieSecure())
                .build();
    }



    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtConfigurationProperties.getJwtCookieName(), null).path(jwtConfigurationProperties.getJwtCookiePath()).build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtConfigurationProperties.getRefreshCookieName(), null).path(jwtConfigurationProperties.getRefreshCookiePath()).build();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigurationProperties.getSecret()));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            log.error(e.getMessage());
        }

        return false;
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
