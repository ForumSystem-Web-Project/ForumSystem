package com.example.forumsystem.service;

import com.example.forumsystem.exeptions.DuplicateEntityException;
import com.example.forumsystem.exeptions.EntityNotFoundException;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.UserRepository;
import com.example.forumsystem.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("TestUser");
    }

    @Test
    void testGetAll() {
        when(userRepository.getAll())
                .thenReturn(List.of(user));

        List<User> users = userService.getAll();

        assertEquals(1, users.size());
        verify(userRepository, times(1)).getAll();
    }

    @Test
    void testGetById() {
        when(userRepository.getById(1))
                .thenReturn(user);

        User foundUser = userService.getById(1);

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).getById(1);
    }

    @Test
    void testGetByName() {
        when(userRepository.getByName("TestUser"))
                .thenReturn(user);

        User foundUser = userService.getByName("TestUser");

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).getByName("TestUser");
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.getByName("TestUser"))
                .thenThrow(new EntityNotFoundException("User", "username", "TestUser"));

        doNothing().when(userRepository).createUser(user);

        assertDoesNotThrow(() -> userService.createUser(user));
        verify(userRepository, times(1)).createUser(user);
    }

    @Test
    void testCreateUser_DuplicateEntityException() {
        when(userRepository.getByName("TestUser"))
                .thenReturn(user);

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(user));
    }

    @Test
    void testUpdateUser() {
        doNothing().when(userRepository).updateUser(user);

        assertDoesNotThrow(() -> userService.updateUser(user));
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteUser(1);

        assertDoesNotThrow(() -> userService.deleteUser(1));
        verify(userRepository, times(1)).deleteUser(1);
    }
}
