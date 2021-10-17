package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.Role;
import com.example.shopmecommon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testCreateUser() {
        Role role = testEntityManager.find(Role.class, Long.parseLong("2"));
//        User saveUser = User.builder().email("Minhvu@gmail.com")
//                .password("123456789")
//                .firstName("Hoang")
//                .lastName("Vu")
//                .build();
        User user = new User("Minhvu12918@gmail.com", "123456789","Minh", "Vu");
        user.addRole(role);
        User saveUser = userRepository.save(user);
        assertThat(saveUser.getId()).isGreaterThan(0);
    }

    @Test
    void testCreateUserWithManyRole() {
        Role roleAssistant = testEntityManager.find(Role.class, Long.parseLong("3"));
        Role roleEditor = testEntityManager.find(Role.class, Long.parseLong("4"));
        User user = new User("ThoTuan@gmail.com", "123456789","Tho", "Tuan");
        user.addRole(roleAssistant);
        user.addRole(roleEditor);
        User saveUser = userRepository.save(user);
        assertThat(saveUser.getId()).isGreaterThan(0);
    }

    @Test
    void testFindAllUser() {
        Iterable<User> users = userRepository.findAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    void testGetUser() {
        User user = userRepository.findById(Long.parseLong("2")).get();
        assertThat(user).isNotNull();
    }

    @Test
    void testUpdateUser() {
        User user = userRepository.findById(Long.parseLong("2")).get();
        user.setEmail("Xuantun@gmail.com");
        userRepository.save(user);
    }

    @Test void testFindUserByEmail() {
        User user = userRepository.findByEmail("Minhvu12918@gmail.com");
        assertThat(user).isNotNull();
    }
}