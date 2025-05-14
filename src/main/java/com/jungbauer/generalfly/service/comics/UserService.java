package com.jungbauer.generalfly.service.comics;

import com.jungbauer.generalfly.domain.User;
import com.jungbauer.generalfly.dto.UserDto;
import com.jungbauer.generalfly.repository.RoleRepository;
import com.jungbauer.generalfly.repository.UserRepository;
import com.jungbauer.generalfly.utils.UserAlreadyExistsException;
import com.jungbauer.generalfly.utils.UserRoles;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User registerNewUserAccount(final UserDto userDto) throws UserAlreadyExistsException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        final User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRoles(Set.of(roleRepository.findByName(UserRoles.USER.getText())));

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
