package com.example.forumsystem.service;

import com.example.forumsystem.exceptions.DuplicateEntityException;
import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.helpers.PermissionHelpers;
import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public List<PhoneNumber> getAll(){
        return phoneNumberRepository.getAll();
    }

    @Override
    public PhoneNumber getByUserId(User admin) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return phoneNumberRepository.getByUserId(admin);
    }

    @Override
    public PhoneNumber getByPhoneNumber (User admin, PhoneNumber phoneNumber) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return phoneNumberRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public void createPhoneNumber(User admin, PhoneNumber phoneNumber){
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        boolean adminHasPhoneNumber = true;
        boolean anotherAdminHasPhoneNumber = true;

        try {
            phoneNumberRepository.getByUserId(admin);
        } catch (EntityNotFoundException e) {
            adminHasPhoneNumber = false;
        }

        try {
            phoneNumberRepository.getByPhoneNumber(phoneNumber);
        } catch (EntityNotFoundException e) {
            anotherAdminHasPhoneNumber = false;
        }

        if (adminHasPhoneNumber) {
            throw new InvalidOperationException("This admin already has a phone number!");
        } else if (anotherAdminHasPhoneNumber) {
            throw new DuplicateEntityException("Admin", "phone number", phoneNumber.getPhoneNumber());
        }

        phoneNumber.setCreatedBy(admin);
        phoneNumberRepository.create(phoneNumber);
    }

    @Override
    public void updatePhoneNumber(User admin, PhoneNumber phoneNumber){
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);

        PhoneNumber existingPhone = phoneNumberRepository.getByUserId(admin);
        if (phoneNumber.getPhoneNumber().equals(existingPhone.getPhoneNumber())) {
            throw new InvalidOperationException("Invalid operation! User's number is unchanged!");
        }

        boolean exists = true;
        try {
            phoneNumberRepository.getByPhoneNumber(phoneNumber);
        } catch (EntityNotFoundException e){
            exists = false;
        }

        if (exists){
            throw new DuplicateEntityException("Admin", "phone number", phoneNumber.getPhoneNumber());
        }

        phoneNumberRepository.update(phoneNumber);
    }

    @Override
    public void deletePhoneNumber(User admin){
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        PhoneNumber phoneNumber = phoneNumberRepository.getByUserId(admin);
        phoneNumberRepository.delete(phoneNumber);
    }
}
