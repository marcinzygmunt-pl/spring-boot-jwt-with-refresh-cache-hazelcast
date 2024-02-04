package pl.marcinzygmunt.jwt.model.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinzygmunt.jwt.model.entity.RefreshToken;
import pl.marcinzygmunt.jwt.model.service.RefreshTokenService;
import pl.marcinzygmunt.jwt.util.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);
        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            RefreshToken validRefresh = refreshTokenService.verifyExpiration(refreshToken);
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(validRefresh.getUserAccount());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body("Token is refreshed successfully!");
        }

        return ResponseEntity.badRequest().body("Refresh Token is empty!");
    }
}
