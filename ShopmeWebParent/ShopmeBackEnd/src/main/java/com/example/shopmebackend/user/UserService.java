package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<Role> getAllRoles() {return roleRepository.findAll();}
    public User saveUser(User user) {
        encoderPassword(user);
        return userRepository.save(user);
    }
    public void encoderPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
