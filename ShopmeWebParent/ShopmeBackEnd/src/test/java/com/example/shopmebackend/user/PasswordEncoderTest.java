package com.example.shopmebackend.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PasswordEncoderTest {

    @Test
    public void testEncocoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = "Minhvu1712918";
        String passwordEncoder = bCryptPasswordEncoder.encode(password);
        System.out.println(password);
        boolean test = bCryptPasswordEncoder.matches(password, passwordEncoder);
        assertThat(test).isTrue();
    }
}
