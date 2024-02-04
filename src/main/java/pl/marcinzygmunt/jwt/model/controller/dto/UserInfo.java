package pl.marcinzygmunt.jwt.model.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

import java.util.Set;

@Getter
@Setter
public class UserInfo {
    private String username;
    private Set<String> roles;
}
