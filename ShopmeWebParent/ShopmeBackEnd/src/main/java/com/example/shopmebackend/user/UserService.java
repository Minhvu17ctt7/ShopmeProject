package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
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
        if(user.getId() != null) {
            if(user.getPassword().isEmpty()) {
                User existingUser = userRepository.findById(user.getId()).get();
                user.setPassword(existingUser.getPassword());
            }else {
                encoderPassword(user);
            }
        } else {
            encoderPassword(user);
        }
        return userRepository.save(user);
    }
    public void encoderPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    public boolean isEmailUnique(Long id,String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return true;
        }
        if(id == null) {
            if(user != null) {
                return false;
            }
        }else {
            if(user.getId() != id) return false;
        }
        return true;
    }
    public User get(Long id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not found user with id: " + id);
        }

    }

    public void deleteUser(Long id) throws UserNotFoundException {
        Long count = userRepository.countUsersById(id);
        if(count == null || count == 0) {
            throw new UserNotFoundException("Could not find any user with Id :" + id);
        }
        userRepository.deleteById(id);
    }

    public void updateEnabledStatus(Long id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }
}
