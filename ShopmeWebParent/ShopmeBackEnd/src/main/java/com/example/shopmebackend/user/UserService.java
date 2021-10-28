package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public static final int USER_SIZE_PAGE = 4;

    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by("firstName").ascending());
    }
    public List<Role> getAllRoles() {return roleRepository.findAll();}
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
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

    public User updateUser(User userInform) {
        User user = userRepository.findById(userInform.getId()).get();
        if(!userInform.getPassword().isEmpty()) {
            user.setPassword(userInform.getPassword());
            encoderPassword(user);
        }
        if(userInform.getPhotos() != null) {
            user.setPhotos(userInform.getPhotos());
        }
        user.setFirstName(userInform.getFirstName());
        user.setLastName(userInform.getLastName());
        return userRepository.save(user);
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

    public Page<User> getListUserPagination(int pageNumber,String sortField, String sortName, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortName.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, USER_SIZE_PAGE, sort);
        if(keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }
}
