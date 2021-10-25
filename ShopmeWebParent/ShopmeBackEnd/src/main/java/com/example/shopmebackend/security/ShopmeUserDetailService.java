package com.example.shopmebackend.security;

import com.example.shopmebackend.user.UserRepository;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopmeUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            return new ShopmeUserDetail(user);

        }
    throw new UsernameNotFoundException("Not found user with email " + email);
    }
}
