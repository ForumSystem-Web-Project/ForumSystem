package com.example.forumsystem.service;

import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.User;

import java.util.List;

public interface PhoneNumberService {
    List<PhoneNumber> getAll();

    PhoneNumber getByUserId(User admin);

    PhoneNumber getByPhoneNumber (User admin, PhoneNumber phoneNumber);

    void createPhoneNumber(User admin, PhoneNumber phoneNumber);

    void updatePhoneNumber(User admin, PhoneNumber phoneNumber);

    void deletePhoneNumber(User admin);
}
