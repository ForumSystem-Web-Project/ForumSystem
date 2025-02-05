package com.example.forumsystem.service;

import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.exeptions.UnauthorizedOperationException;
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
    public PhoneNumber getByUserId(User admin, User user) {
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        return phoneNumberRepository.getByUserId(user);
    }

    @Override
    public void createPhoneNumber(User admin, PhoneNumber phoneNumber){
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        boolean exists = true;

        try {
            phoneNumberRepository.getByUserId(admin);
        } catch (EntityNotFoundException e){
            exists = false;
        }

        if (exists){
            throw new UnauthorizedOperationException("This admin already has a phone number!");
        }

        phoneNumber.setCreatedBy(admin);

        phoneNumberRepository.create(phoneNumber);
    }

    @Override
    public void updatePhoneNumber(User admin, PhoneNumber phoneNumber){
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        phoneNumberRepository.getByUserId(admin);
        phoneNumberRepository.update(phoneNumber);
    }

    @Override
    public void deletePhoneNumber(User admin, PhoneNumber phoneNumber){
        PermissionHelpers.checkIfBlocked(admin);
        PermissionHelpers.checkIfAdmin(admin);
        phoneNumberRepository.getByUserId(admin);
        phoneNumberRepository.delete(phoneNumber);
    }
}
