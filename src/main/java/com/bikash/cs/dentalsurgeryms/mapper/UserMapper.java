package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.UserResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(source = "password", target = "password")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "stringListToRoleList")
    User userRequestDtoToUser(UserRequestDto userRequestDto);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "roleListToStringList")
    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> userToUserResponseDto(List<User> users);

    @Named("stringListToRoleList")
    default List<Role> stringListToRoleList(List<String> roleStrings) {
        if (roleStrings == null) return null;
        return roleStrings.stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());
    }

    @Named("roleListToStringList")
    default List<String> roleListToStringList(List<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toList());
    }
}
