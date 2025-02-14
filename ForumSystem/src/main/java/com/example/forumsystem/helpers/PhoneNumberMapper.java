package com.example.forumsystem.helpers;

import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.PhoneNumberDto;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {

        private final PhoneNumberRepository phoneNumberRepository;

        @Autowired
    public PhoneNumberMapper(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    public PhoneNumber map(PhoneNumberDto phoneNumberDTO) {
        return dtoToObject(phoneNumberDTO);
    }

    public PhoneNumber updateMap(PhoneNumberDto phoneNumberDto, User admin) {
            return dtoToObjectForUpdate(phoneNumberDto, admin);
    }

    private PhoneNumber dtoToObject(PhoneNumberDto phoneNumberDto) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(phoneNumberDto.getNumber());
        return phoneNumber;
    }

    private PhoneNumber dtoToObjectForUpdate(PhoneNumberDto phoneNumberDto, User admin) {
        PhoneNumber phoneNumber = phoneNumberRepository.getByUserId(admin);
        phoneNumber.setPhoneNumber(phoneNumberDto.getNumber());
        return phoneNumber;
    }
}
