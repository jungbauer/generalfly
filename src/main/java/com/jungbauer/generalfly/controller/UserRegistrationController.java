package com.jungbauer.generalfly.controller;

import com.jungbauer.generalfly.domain.User;
import com.jungbauer.generalfly.dto.UserDto;
import com.jungbauer.generalfly.service.comics.UserService;
import com.jungbauer.generalfly.utils.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/userRegistration";
    }

    @PostMapping("/user/registration")
    public String registerUserAccount(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/userRegistration";
        }

        try {
            User registered = userService.registerNewUserAccount(userDto);
            model.addAttribute("regUsername", registered.getFirstName() + " " + registered.getLastName());
            model.addAttribute("regEmail", registered.getEmail());
        } catch (UserAlreadyExistsException uaeEx) {
            bindingResult.addError(new ObjectError("user", "An account for that username/email already exists."));
            return "user/userRegistration";
        }
        catch (Exception ex) {
            return "error";
        }

        return "user/userRegistrationSuccess";
    }
}
