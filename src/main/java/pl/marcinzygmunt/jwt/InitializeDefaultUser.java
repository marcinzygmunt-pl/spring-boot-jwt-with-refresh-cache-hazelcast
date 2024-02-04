package pl.marcinzygmunt.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pl.marcinzygmunt.jwt.model.UserRole;
import pl.marcinzygmunt.jwt.model.controller.dto.SignupRequest;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;
import pl.marcinzygmunt.jwt.model.repsitory.UserAccountRepository;
import pl.marcinzygmunt.jwt.model.service.UserService;

import java.util.Optional;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "pl.marcinzygmunt.default-user.create", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class InitializeDefaultUser implements InitializingBean {

    private final UserAccountRepository userAccountRepository;

    private final UserService userService;

    private final DefaultUserConfigurationProperties defaultUserConfigurationProperties;

    @Override
    public void afterPropertiesSet() {
        Optional<UserAccountEntity> userAccountEntity = userAccountRepository.findByEmail(defaultUserConfigurationProperties.getEmail());
        if (userAccountEntity.isPresent() && defaultUserConfigurationProperties.isOverrideUser()) {
            UserAccountEntity updatedUserAccount = userAccountEntity.get();
            updatedUserAccount.setEmail(defaultUserConfigurationProperties.getEmail());
            updatedUserAccount.setPassword(defaultUserConfigurationProperties.getPassword());
            updatedUserAccount.setRoles(Set.of(UserRole.ROLE_ADMIN));
            userAccountRepository.save(updatedUserAccount);
        } else {
            SignupRequest defaultUserSignup =
                    SignupRequest.builder()
                            .email(defaultUserConfigurationProperties.getEmail())
                            .password(defaultUserConfigurationProperties.getPassword())
                            .roles(Set.of("ROLE_ADMIN"))
                            .build();
            userService.createUser(defaultUserSignup);
        }
        log.info("DEFAULT USER ADDED");
    }
}
