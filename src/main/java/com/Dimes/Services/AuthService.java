package com.Dimes.Services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.Dimes.Models.Lender;
import com.Dimes.Repositories.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService
 */
@Service
@Transactional
public class AuthService {

    private AuthRepository authRepository;

    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Boolean RegisterLender(Lender lender) {
        Objects.requireNonNull(lender, "Lender can not be null");

        lender.setPassword(passwordEncoders().encode(lender.getPassword()));
        Lender savedLender = authRepository.saveAndFlush(lender);

        if (savedLender != null) {
            return true;
        } else {
            return false;
        }

    }

    // get userId by username
    public Optional<Integer> getUserId(String username) {
        return authRepository.findUserId(username);
    }

    // get user first name by username
    public String getUserFirstName(String username) {
        return authRepository.findFirstName(username).orElse("First name for " + username + " not found");
    }

    public List<Lender> getAllLenderDetails() {
        return authRepository.findAll();
    }

    public boolean checkIfLenderExists(String username) {
        if (authRepository.findByUsername(username) != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        return email.matches(regex);
    }

    @Bean
    PasswordEncoder passwordEncoders() {
        return new BCryptPasswordEncoder();
    }

    // lebon added this
    public Lender GetLender(String username) {
        Lender lender = authRepository.findByUsername(username);
        return lender;
    }

    // lebon added this
    public boolean updateLender(Lender lender) {

        Objects.requireNonNull(lender, "Lender will never be null");

        Lender lender1 = authRepository.save(lender);
        if (lender1 != null) {
            return true;
        }
        return false;
    }

}