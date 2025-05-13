package com.jungbauer.generalfly.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotNull(message = "First name cannot be null.")
    @NotEmpty(message = "First name cannot be empty.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    @NotEmpty(message = "Last name cannot be empty.")
    private String lastName;

    @NotNull(message = "Password cannot be null.")
    @NotEmpty(message = "Password cannot be empty.")
    private String password;
    private String matchingPassword;

    @Email
    @NotNull(message = "Email cannot be null.")
    @NotEmpty(message = "Email cannot be empty.")
    private String email;

    public UserDto() {
    }

    @Override
    public String toString() {
        return "UserDto [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
    }
}
