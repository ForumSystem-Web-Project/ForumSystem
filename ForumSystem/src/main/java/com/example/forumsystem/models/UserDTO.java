package com.example.forumsystem.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UserDTO {

    @NotNull(message = "First name can't be empty")
    @Size(min = 4, max = 32, message = "Name should be between 2 and 20 symbols")
    private String firstName;

    @NotNull(message = "Last name can't be empty")
    @Size(min = 4, max = 32, message = "Name should be between 2 and 20 symbols")
    private String lastName;

    @Email
    @Valid
    private String email;

    @NotNull(message = "Username can not be empty")
    private String username;

    @NotNull(message = "Password can not be empty")
    private String password;

    public UserDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
