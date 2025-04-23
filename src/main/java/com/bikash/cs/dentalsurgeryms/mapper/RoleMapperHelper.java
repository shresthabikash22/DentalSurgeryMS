package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.enums.Role;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapperHelper {
    @Named("stringSetToRoleSet")
    public Set<Role> stringSetToRoleSet(Set<String> roleStrings) {
        if (roleStrings == null) return null;
        return roleStrings.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    @Named("roleSetToStringSet")
    public Set<String> roleSetToStringSet(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
}
