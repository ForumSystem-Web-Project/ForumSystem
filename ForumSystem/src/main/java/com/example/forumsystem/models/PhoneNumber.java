package com.example.forumsystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_number_id")
    private int phoneNumberId;

    @Column(name = "phone_number")
    //To be put in the DTO:
    @NotNull
    @Pattern (regexp = "^\\+?[1-9]\\d{0,2}[\\s.-]?(\\(?\\d{1,4}\\)?)?[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,9}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    public PhoneNumber() {
    }

    public int getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(int phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber() {
        this.phoneNumber = phoneNumber;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
