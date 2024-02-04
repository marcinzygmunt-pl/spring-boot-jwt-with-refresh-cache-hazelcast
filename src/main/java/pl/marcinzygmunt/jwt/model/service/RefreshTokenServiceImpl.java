package pl.marcinzygmunt.jwt.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.marcinzygmunt.jwt.JwtConfigurationProperties;
import pl.marcinzygmunt.jwt.model.entity.RefreshTokenEntity;
import pl.marcinzygmunt.jwt.model.repsitory.RefreshTokenRepository;
import pl.marcinzygmunt.jwt.model.repsitory.UserAccountRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userRepository;
    private final JwtConfigurationProperties jwtConfigurationProperties;

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity createRefreshToken(String email) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUserAccount(userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found")));
        refreshToken.setValidDate(LocalDateTime.now(Clock.systemDefaultZone()).plusMinutes(jwtConfigurationProperties.getRefreshExpirationMin()));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshToken) {
        if (refreshToken.getValidDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(String.format("%s Refresh token was expired. Please make a new signin request",refreshToken.getToken()));
        }
        return refreshToken;
    }

    @Override
    @Transactional
    public int deleteByUserEmail(String email) {
            return refreshTokenRepository.deleteByUserAccount(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")));
      }
}
