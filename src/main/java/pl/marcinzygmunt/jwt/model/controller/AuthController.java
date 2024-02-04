package pl.marcinzygmunt.jwt.model.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.marcinzygmunt.jwt.model.controller.dto.LoginRequest;
import pl.marcinzygmunt.jwt.model.controller.dto.SignupRequest;
import pl.marcinzygmunt.jwt.model.controller.dto.UserInfo;
import pl.marcinzygmunt.jwt.model.entity.RefreshTokenEntity;
import pl.marcinzygmunt.jwt.model.service.RefreshTokenService;
import pl.marcinzygmunt.jwt.model.service.UserService;
import pl.marcinzygmunt.jwt.security.UserDetailsImpl;
import pl.marcinzygmunt.jwt.util.JwtUtils;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("Login successful!");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        userService.createUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principle.equals("anonymousUser")) {
            refreshTokenService.deleteByUserEmail(((UserDetailsImpl) principle).getUsername());
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie refreshCookie = jwtUtils.getCleanJwtRefreshCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("You've been signed out!");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> me() {
        return ResponseEntity.ok().body(userService.getCurrentUserInfo());
    }

}
