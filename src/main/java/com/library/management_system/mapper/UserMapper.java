package com.library.management_system.mapper;

import com.library.management_system.dto.UserCreationDTO;
import com.library.management_system.dto.valueobject.PlainPassword;
import com.library.management_system.model.User;
import com.library.management_system.model.embeddable.Username;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserCreationDTO userCreationDTO) {
        final String username = Username.validateUsername(userCreationDTO.username());
        final String password = PlainPassword.validatePlainPassword(userCreationDTO.plainPassword());
        final User user = new User();
        user.setUsername(new Username(username));
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }
}
