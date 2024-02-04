package pl.marcinzygmunt.jwt.model.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class SignupRequest {
    private String email;
    private String password;
    private Set<String> roles;
}
