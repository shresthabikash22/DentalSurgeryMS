package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.UserResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.exception.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.InvalidRoleException;
import com.bikash.cs.dentalsurgeryms.exception.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.UserMapper;
import com.bikash.cs.dentalsurgeryms.model.User;
import com.bikash.cs.dentalsurgeryms.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;


    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .username("validUser")
                .password("validPassword123")
                .roles(List.of(Role.MANAGER))
                .build();

        userRequestDto = new UserRequestDto(
                "validUser",
                "validPassword123",
                List.of(Role.MANAGER.name())
        );

        userResponseDto = new UserResponseDto(1L,"validUser", List.of(Role.MANAGER.name()));

    }

    @Test
    @DisplayName("Create user when username does not exist")
    void givenUserRequestDto_whenCreate_thenReturnSavedUserResponseDto() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        when(userMapper.userRequestDtoToUser(userRequestDto)).thenReturn(user);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto savedUserDto = userService.createUser(userRequestDto);

        Mockito.verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, times(1)).save(user);
        Assertions.assertThat(savedUserDto).isEqualTo(userResponseDto);
    }

    @Test
    @DisplayName("Create user with invalid role should throw InvalidRoleException")
    void createUser_withInvalidRole_thenThrowInvalidRoleException() {
        UserRequestDto badRoleDto = new UserRequestDto(
                "anotherUser",
                "somePassword123",
                List.of("NOT_A_ROLE")
        );
        when(userRepository.findByUsername(badRoleDto.username())).thenReturn(Optional.empty());
        assertThrows(InvalidRoleException.class, () -> userService.createUser(badRoleDto));
        verify(userRepository, times(1)).findByUsername(badRoleDto.username());
    }

    @Test
    @DisplayName("Create user when username exists should throw DuplicateResourceException")
    void givenExistingUser_whenCreate_thenThrowDuplicateResourceException() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(user));
        assertThrows(DuplicateResourceException.class, () -> userService.createUser(userRequestDto));
        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Get user by username when exists")
    void givenExistingUser_whenGetUserByUsername_thenReturnUserResponseDto() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto response = userService.getUserByUsername(userRequestDto.username());

        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        Assertions.assertThat(response).isEqualTo(userResponseDto);
    }

    @Test
    @DisplayName("Get user by username when does not exist should throw exception")
    void givenNonExistingUser_whenGetUserByUsername_thenThrowException() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername(userRequestDto.username()));
        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
    }

    @Test
    @DisplayName("Get all users should return a page of response dtos")
    void givenExistingUsers_whenGetAllUsers_thenReturnPageOfUserResponseDtos() {
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "username");
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        Page<UserResponseDto> result = userService.getAllUsers(0, 2, "ASC", "username");
        Assertions.assertThat(result.getContent()).containsExactly(userResponseDto);
    }

    @Test
    @DisplayName("Update user when username exists should return updated response dto")
    void givenExistingUser_whenUpdateUser_thenReturnUpdatedUserResponseDto() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(user));
        when(userMapper.userRequestDtoToUser(userRequestDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto updatedDto = userService.updateUser(userRequestDto.username(), userRequestDto);

        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, times(1)).save(user);
        Assertions.assertThat(updatedDto).isEqualTo(userResponseDto);
    }

    @Test
    @DisplayName("Update user when username does not exist should throw exception")
    void givenNonExistingUser_whenUpdateUser_thenThrowException() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userRequestDto.username(), userRequestDto));
        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Delete user by username when exists")
    void givenExistingUser_whenDeleteUserByUsername_thenDeleteUser() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(user));
        userService.deleteByUserName(userRequestDto.username());
        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, times(1)).deleteByUsername(userRequestDto.username());
    }

    @Test
    @DisplayName("Delete user by username when does not exist should throw exception")
    void givenNonExistingUser_whenDeleteUserByUsername_thenThrowException() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteByUserName(userRequestDto.username()));
        verify(userRepository, times(1)).findByUsername(userRequestDto.username());
        verify(userRepository, never()).deleteByUsername(anyString());
    }






}