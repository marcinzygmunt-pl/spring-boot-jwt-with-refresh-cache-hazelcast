package pl.marcinzygmunt.jwt.model.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import pl.marcinzygmunt.jwt.model.UserRole;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user_accounts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")})

public class UserAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @CreatedDate
    LocalDateTime createDate;

    boolean active = true;

    @ElementCollection(targetClass = UserRole.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();
}

