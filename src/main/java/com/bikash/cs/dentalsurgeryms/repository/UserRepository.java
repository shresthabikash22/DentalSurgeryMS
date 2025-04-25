package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);

    Page<User> findAll(Pageable pageable);
}
