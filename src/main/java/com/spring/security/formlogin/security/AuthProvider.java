package com.spring.security.formlogin.security;

import com.spring.security.formlogin.model.Attempts;
import com.spring.security.formlogin.model.User;
import com.spring.security.formlogin.repository.AttemptsRepository;
import com.spring.security.formlogin.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component public class AuthProvider implements AuthenticationProvider {

    private static final int ATTEMPTS_LIMIT = 3;
    @Autowired
    private SecurityUserDetailService userDetailService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AttemptsRepository attemptsRepository;
    @Autowired private UserRespository userRespository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> userOptional = userRespository.findUserByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            processFailedAttempts(username, user);
            if(user.getPassword().equals(password)){
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    private void processFailedAttempts(String username, User user){
        Optional<Attempts> userAttempts
                = attemptsRepository.findAttemptsByUsername(username);
        if(userAttempts.isEmpty()){
            Attempts attempts = new Attempts();
            attempts.setUsername(username);
            attempts.setAttempts(1);
            attemptsRepository.save(attempts);
        }else{
            Attempts attempts = userAttempts.get();
            attempts.setAttempts(attempts.getAttempts() + 1);
            attemptsRepository.save(attempts);
            if (attempts.getAttempts() + 1 >
                    ATTEMPTS_LIMIT) {
                user.setAccountNonLocked(false);
                userRespository.save(user);
                throw new LockedException("Too many invalid attempts. Account is locked!!");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
