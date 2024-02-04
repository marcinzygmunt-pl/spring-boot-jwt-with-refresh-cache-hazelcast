package pl.marcinzygmunt.jwt.model.repsitory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import pl.marcinzygmunt.jwt.model.entity.RefreshToken;

import java.util.List;

public interface RefreshTokenRepository {
    @Cacheable(value = "refreshTokens", key="#token")
    public RefreshToken getToken(String token);

    public List<RefreshToken> getTokens();

    @CachePut(cacheNames = "refreshTokens", key = "#refreshToken.token")
    public RefreshToken updateRefreshToken(RefreshToken refreshToken);

    @CacheEvict(cacheNames = "refreshTokens", key = "#token")
    public String deleteRefreshToken(String token);

}
