package com.example.forumsystem.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto extends LoginDto{

    @NotEmpty
    private String passwordConfirm;

    @NotEmpty
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols!")
    private String firstName;

    @NotEmpty
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols!")
    private String lastName;

    @NotEmpty
    private String email;

    public RegisterDto() {
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
}
