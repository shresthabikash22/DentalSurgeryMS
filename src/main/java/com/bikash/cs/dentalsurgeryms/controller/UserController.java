package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.UserResponseDto;
import com.bikash.cs.dentalsurgeryms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
            UserResponseDto createdUser = userService.createUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Page<UserResponseDto> usersPage = userService.getAllUsers(page, pageSize, sortDirection, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(usersPage);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String username, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(username, userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable String username) {
        userService.deleteByUserName(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
