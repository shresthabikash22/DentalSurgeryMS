package com.bikash.cs.dentalsurgeryms.service;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserByUsername(String name);
    UserResponseDto updateUser(String username, UserRequestDto userRequestDto);
    void deleteByUserName(String name);
    Page<UserResponseDto> getAllUsers(int page,int pageSize,String sortDirection,String sortBy);
}
