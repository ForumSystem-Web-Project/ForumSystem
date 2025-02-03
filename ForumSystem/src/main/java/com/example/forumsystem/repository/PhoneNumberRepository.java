package com.example.forumsystem.repository;

import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.User;

import java.util.List;

public interface PhoneNumberRepository {

    List<PhoneNumber> getAll();

    PhoneNumber getByUserId(User user);

    void create(PhoneNumber phoneNumber);

    void update(PhoneNumber phoneNumber);

    void delete(PhoneNumber phoneNumber);
}
