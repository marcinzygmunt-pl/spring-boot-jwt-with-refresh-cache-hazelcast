package pl.marcinzygmunt.jwt.model.service;

import jakarta.transaction.Transactional;
import pl.marcinzygmunt.jwt.model.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);
    RefreshToken createRefreshToken(String username);
    RefreshToken verifyExpiration(String token);
    void deleteByUserEmail(String email);
}
