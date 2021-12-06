package com.example.shopmebackend.user;

import com.example.shopmebackend.user.repository.RoleRepository;
import com.example.shopmecommon.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole() {
        Role role = new Role("ADMIN", "Manage everything");
        Role savedRole = roleRepository.save(role);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateAllRoles() {
        Role roleSalePerson = new Role("SalesPerson", "manage product price"
                + "customers, shipping, orders and sales repport");
        Role roleEditor = new Role("Editor", "manage categories" + "products, articles and menus");
        Role roleShipper = new Role("Shipper", "view products, view orders");
        Role roleAssitstant = new Role("Assistant", "manage questions and reviews");
        roleRepository.saveAll(List.of(roleAssitstant, roleEditor, roleShipper, roleSalePerson));
    }
}
