package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.UserResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.exception.role.DuplicateRoleException;
import com.bikash.cs.dentalsurgeryms.exception.role.InvalidRoleException;
import com.bikash.cs.dentalsurgeryms.exception.user.DuplicateUserException;
import com.bikash.cs.dentalsurgeryms.exception.user.UserNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.UserMapper;
import com.bikash.cs.dentalsurgeryms.model.User;
import com.bikash.cs.dentalsurgeryms.repository.UserRepository;
import com.bikash.cs.dentalsurgeryms.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private void validateRoles(List<String> roleStrings){
        if(roleStrings!=null){
            for (String role : roleStrings){
                try{
                    Role.valueOf(role);
                }catch (IllegalArgumentException e){
                    throw new InvalidRoleException("Invalid role '" + role + "'.");
                }
            }

            if(new HashSet<>(roleStrings).size() != roleStrings.size()){
                throw new DuplicateRoleException("Duplicate roles found.");
            }
        }
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.findByUsername(userRequestDto.username()).isPresent()){
            throw new DuplicateUserException("Username '" + userRequestDto.username() + "' is already taken");
        }
        validateRoles(userRequestDto.roles());

        User savedUser = userRepository.save(userMapper.userRequestDtoToUser(userRequestDto));
        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found."));
        return userMapper.userToUserResponseDto(user);
    }


    @Override
    public Page<UserResponseDto> getAllUsers(int page, int pageSize, String sortDirection, String sortBy){
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::userToUserResponseDto);
    }

    @Override
    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username '" + username+ "' not found."));
        if(!existingUser.getUsername().equals(userRequestDto.username()) &&
                userRepository.findByUsername(userRequestDto.username()).isPresent()){
                throw new DuplicateUserException("Username '" + userRequestDto.username() + "' is already taken");
        }
        validateRoles(userRequestDto.roles());

        User updatedUser = userMapper.userRequestDtoToUser(userRequestDto);
        updatedUser.setUserId(existingUser.getUserId());
        User savedUser =  userRepository.save(updatedUser);
        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteByUserName(String name) {
        Optional<User> optionalUser = userRepository.findByUsername(name);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            userRepository.deleteByUsername(user.getUsername());
        }else{
            throw new UserNotFoundException("User with username '" + name + "' not found.");
        }
    }

}
