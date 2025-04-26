package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.model.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    User user, user1;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("RonMiller57")
                .password("secretpass3#")
                .roles(List.of(Role.DENTIST))
                .build();

        user1 = User.builder().username("RonMiller571").password("<PASSWORD>#").roles(List.of(Role.PATIENT)).build();

    }
    @Test
    @DisplayName("test for saving new user")
    void givenNonExistingUser_whenCreateUser_thenUserResponseDto() {
        User savedUser = userRepository.saveAndFlush(user);
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getRoles(), savedUser.getRoles());
    }

    @Test
    @DisplayName("test for saving existing user")
    void givenExistingUser_whenCreateUser_thenThrowException() {
        User savedUser = userRepository.saveAndFlush(user);
        User user2 = user.builder()
                .username("RonMiller57")
                .password("this<PASSWORD>#")
                .roles(List.of(Role.DENTIST))
                .build();
        assertThrows(DataIntegrityViolationException.class,()-> userRepository.saveAndFlush(user2));
    }

    @Test
    @DisplayName("test for finding user by username")
    void givenExistingUser_whenFindByUsername_thenReturnUserResponseDTo() {
        User savedUser = userRepository.saveAndFlush(user);
        User foundUser = userRepository.findByUsername(savedUser.getUsername()).orElse(null);
        assertNotNull(foundUser);
        assertEquals(savedUser.getUsername(), foundUser.getUsername());
        assertEquals(savedUser.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("test for finding user by username")
    void givenNonExistingUser_whenFindByUsername_thenReturnNull() {
        User foundUser = userRepository.findByUsername("nonExistingUser").orElse(null);
        assertNull(foundUser);
    }

    @Test
    @DisplayName("test for finding all users")
    void givenUsers_whenFindAll_thenReturnList() {
        User savedUser = userRepository.saveAndFlush(user);
        List<User> foundUsers = userRepository.findAll();
        assertNotNull(foundUsers);
        assertTrue(foundUsers.contains(savedUser));
    }

    @Test
    @DisplayName("test for deleting user")
    void givenExistingUser_whenDelete_thenUserResponseDto() {
        User savedUser = userRepository.saveAndFlush(user);
        userRepository.delete(savedUser);
        User foundUser = userRepository.findByUsername(savedUser.getUsername()).orElse(null);
        assertNull(foundUser);
    }


    @Test
    @DisplayName("find list of users using page")
    void givenExistingUsers_whenFindAll_thenReturnPaginatedUsersList(){
        userRepository.saveAllAndFlush(List.of(user,user1));
        Pageable pageable = PageRequest.of(0,2, Sort.by("username").ascending());
        Page<User> userPage = userRepository.findAll(pageable);
        assertNotNull(userPage);
        assertEquals(2,userPage.getNumberOfElements());
        assertEquals(2, userPage.getTotalElements());
        assertEquals(1, userPage.getTotalPages());
        assertEquals(0, userPage.getNumber());
        assertFalse(userPage.hasNext());
        assertFalse(userPage.hasPrevious());

    }

    @Test@DisplayName("find list of users using page")
    void givenNonExistingUsers_whenFindAll_thenReturnsNoUsers(){
        Pageable pageable = PageRequest.of(1, 2, Sort.by("username").ascending());
        Page<User> userPage = userRepository.findAll(pageable);

        assertNotNull(userPage);
        assertEquals(0, userPage.getNumberOfElements());
        assertEquals(0, userPage.getTotalElements());
        assertEquals(0, userPage.getTotalPages());

        // Requested page is 1 (second page), but data is empty.
        assertEquals(1, userPage.getNumber());

        assertFalse(userPage.hasNext());
        assertTrue(userPage.hasPrevious());

    }


}