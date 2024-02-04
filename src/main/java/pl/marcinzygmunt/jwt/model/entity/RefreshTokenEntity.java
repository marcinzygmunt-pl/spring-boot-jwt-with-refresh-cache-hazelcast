package pl.marcinzygmunt.jwt.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "refresh_token")
@Setter
@Getter
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccountEntity userAccount;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime validDate;

}
