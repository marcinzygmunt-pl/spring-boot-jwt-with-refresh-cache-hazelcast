package pl.marcinzygmunt.jwt.model.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import pl.marcinzygmunt.jwt.model.entity.RefreshTokenEntity;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @Modifying
    int deleteByUserAccount(UserAccountEntity userAccount);

    Optional<RefreshTokenEntity> findByToken(String token);
}
