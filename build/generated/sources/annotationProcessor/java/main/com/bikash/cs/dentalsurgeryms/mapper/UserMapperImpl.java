package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.UserResponseDto;
import com.bikash.cs.dentalsurgeryms.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-23T01:42:31-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userRequestDtoToUser(UserRequestDto userRequestDto) {
        if ( userRequestDto == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( userRequestDto.password() );
        user.setRoles( stringListToRoleList( userRequestDto.roles() ) );
        user.setUsername( userRequestDto.username() );

        return user;
    }

    @Override
    public UserResponseDto userToUserResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long userId = null;
        List<String> roles = null;
        String username = null;

        userId = user.getUserId();
        roles = roleListToStringList( user.getRoles() );
        username = user.getUsername();

        UserResponseDto userResponseDto = new UserResponseDto( userId, username, roles );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> userToUserResponseDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( userToUserResponseDto( user ) );
        }

        return list;
    }
}
