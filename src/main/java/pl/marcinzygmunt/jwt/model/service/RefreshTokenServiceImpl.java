package pl.marcinzygmunt.jwt.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.marcinzygmunt.jwt.JwtConfigurationProperties;
import pl.marcinzygmunt.jwt.model.entity.RefreshToken;
import pl.marcinzygmunt.jwt.model.exception.RefreshExpiredException;
import pl.marcinzygmunt.jwt.model.exception.RefreshTokenNotFoundException;
import pl.marcinzygmunt.jwt.model.exception.UserNotFoundException;
import pl.marcinzygmunt.jwt.model.repsitory.RefreshTokenRepository;
import pl.marcinzygmunt.jwt.model.repsitory.UserAccountRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userRepository;
    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.getToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserAccount(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
        refreshToken.setValidDate(LocalDateTime.now(Clock.systemDefaultZone()).plusMinutes(jwtConfigurationProperties.getRefreshExpirationMin()));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.updateRefreshToken(refreshToken);
        return refreshToken;
    }


    @Override
    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.getToken(token);
        if (refreshToken.getValidDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteRefreshToken(refreshToken.getToken());
            throw new RefreshExpiredException();
        }
        return refreshToken;
    }

    @Override
    public void deleteByUserEmail(String email) {
        String token = refreshTokenRepository.getTokens().stream()
                .filter(x -> x.getUserAccount().getEmail().equals(email))
                .map(RefreshToken::getToken).findFirst().orElseThrow(RefreshTokenNotFoundException::new);
        if (token == null) throw new RefreshTokenNotFoundException();
        refreshTokenRepository.deleteRefreshToken(token);
        refreshTokenRepository.getTokens();
    }
}
