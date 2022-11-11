package com.spring.security.formlogin.repository;

import com.spring.security.formlogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository public interface UserRespository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username); 
}
