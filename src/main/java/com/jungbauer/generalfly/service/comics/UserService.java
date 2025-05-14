package com.jungbauer.generalfly.service.comics;

import com.jungbauer.generalfly.domain.User;
import com.jungbauer.generalfly.dto.UserDto;
import com.jungbauer.generalfly.repository.UserRepository;
import com.jungbauer.generalfly.utils.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
