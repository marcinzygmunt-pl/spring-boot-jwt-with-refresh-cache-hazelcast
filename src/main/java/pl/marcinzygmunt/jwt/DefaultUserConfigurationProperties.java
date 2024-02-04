package pl.marcinzygmunt.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pl.marcinzygmunt.default-user")
@Getter
@Setter
public class DefaultUserConfigurationProperties {
    private String email;
    private String password;
    boolean overrideUser;
}
