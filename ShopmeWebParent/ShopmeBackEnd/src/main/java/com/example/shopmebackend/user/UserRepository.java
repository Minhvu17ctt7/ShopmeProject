package com.example.shopmebackend.user;

import com.example.shopmecommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Long countUsersById(Long id);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnabledStatus(Long id, boolean enabled);

    @Query("SELECT u FROM User u WHERE CONCAT(u.email,' ',u.id,' ',u.firstName,' ',u.lastName) LIKE %?1%" +
    "OR u.email LIKE %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

}
