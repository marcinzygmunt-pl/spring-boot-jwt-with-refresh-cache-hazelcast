package pl.marcinzygmunt.jwt.model.service;

import pl.marcinzygmunt.jwt.model.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenEntity> findByToken(String token);
    RefreshTokenEntity createRefreshToken(String username);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshToken);
    int deleteByUserEmail(String username);
}
