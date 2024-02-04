package pl.marcinzygmunt.jwt.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.marcinzygmunt.jwt.model.controller.dto.SignupRequest;
import pl.marcinzygmunt.jwt.model.controller.dto.UserInfo;
import pl.marcinzygmunt.jwt.model.controller.mapper.UserAccountMapper;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;
import pl.marcinzygmunt.jwt.model.exception.UserExistException;
import pl.marcinzygmunt.jwt.model.exception.UserNotFoundException;
import pl.marcinzygmunt.jwt.model.repsitory.UserAccountRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountMapper userAccountMapper;
    private final UserAccountRepository userRepository;

    @Override
    @Transactional
    public void createUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserExistException();
        }
        userRepository.save(userAccountMapper.toUserAccountEntity(signupRequest));
    }

    @Override
    public UserInfo getCurrentUserInfo() {
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authenticationToken.getPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userDetails.getUsername());
        userInfo.setRoles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return userInfo;
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        UserAccountEntity userAccountEntity = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(userAccountEntity);
    }
}
