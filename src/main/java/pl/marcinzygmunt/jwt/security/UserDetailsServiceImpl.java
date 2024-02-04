package pl.marcinzygmunt.jwt.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;
import pl.marcinzygmunt.jwt.model.repsitory.UserAccountRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

        private final UserAccountRepository userRepository;
        @Override
        @Transactional
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserAccountEntity user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            return UserDetailsImpl.build(user);
        }
}

