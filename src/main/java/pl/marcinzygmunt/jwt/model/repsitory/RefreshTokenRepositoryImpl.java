package pl.marcinzygmunt.jwt.model.repsitory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.marcinzygmunt.jwt.JwtConfigurationProperties;
import pl.marcinzygmunt.jwt.model.entity.RefreshToken;
import pl.marcinzygmunt.jwt.model.exception.RefreshTokenNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final JwtConfigurationProperties jwtConfigurationProperties;

    private final CacheManager cacheManager;
    @Override
    public RefreshToken getToken(String token) {
        Cache refreshTokens = this.cacheManager.getCache(jwtConfigurationProperties.getRefreshCache());
        if (refreshTokens.get(token).get() != null){
            return (RefreshToken) refreshTokens.get(token).get();
        } else {
            throw new RefreshTokenNotFoundException();
        }
    }

    @Override
    public List<RefreshToken> getTokens() {
        Cache refreshTokens = this.cacheManager.getCache(jwtConfigurationProperties.getRefreshCache());
        ConcurrentHashMap<String,RefreshToken> tokenMap =
                (ConcurrentHashMap<String,RefreshToken>) refreshTokens.getNativeCache();
        return new ArrayList<>(tokenMap.values());
    }

    @Override
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return refreshToken;
    }

    @Override
    public String deleteRefreshToken(String token) {
        return token;
    }
}
