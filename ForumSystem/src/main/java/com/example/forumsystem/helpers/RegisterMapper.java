package com.example.forumsystem.helpers;

import com.example.forumsystem.models.RegisterDto;
import com.example.forumsystem.models.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public User fromDto(RegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());

        return user;
    }
}
