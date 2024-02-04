package pl.marcinzygmunt.jwt.model.service;

import pl.marcinzygmunt.jwt.model.controller.dto.SignupRequest;
import pl.marcinzygmunt.jwt.model.controller.dto.UserInfo;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;

import java.security.Principal;

public interface UserService {
    void createUser(SignupRequest signupRequest);
    UserInfo getCurrentUserInfo();
    void removeUser(Long id);
}
