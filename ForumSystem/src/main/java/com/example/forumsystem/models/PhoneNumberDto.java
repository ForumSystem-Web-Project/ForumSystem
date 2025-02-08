package com.example.forumsystem.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PhoneNumberDto {

    @NotNull
    @Size(min = 10, message = "Phone number length can't be less than 10 digits")
    @Pattern(regexp = "^\\+?[1-9]\\d{0,2}[\\s.-]?(\\(?\\d{1,4}\\)?)?[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,9}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String number;

    public PhoneNumberDto() {
    }

    public PhoneNumberDto(String number, String createdBy) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
