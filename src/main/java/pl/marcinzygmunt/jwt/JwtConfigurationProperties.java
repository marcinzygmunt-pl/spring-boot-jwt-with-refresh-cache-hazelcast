package pl.marcinzygmunt.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pl.marcinzygmunt.jwt")
@Getter
@Setter
public class JwtConfigurationProperties {
    private String jwtCookieName;
    private String jwtCookiePath;
    private String refreshCookieName;
    private String refreshCookiePath;
    private boolean cookieSecure;
    private boolean cookieHttp;
    private String secret;
    private int expirationMin;
    private int refreshExpirationMin;
    private String refreshCache;
}
