package pl.marcinzygmunt.jwt.model.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    Optional<UserAccountEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
