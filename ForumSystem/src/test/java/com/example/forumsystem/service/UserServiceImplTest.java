package com.example.forumsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.forumsystem.exceptions.DuplicateEntityException;
import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.helpers.PermissionHelpers;
import com.example.forumsystem.models.FilterUserOptions;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PermissionHelpers permissionHelpers;

    private User admin;
    private User user;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setId(1);
        admin.setAdmin(true);

        user = new User();
        user.setId(2);
        user.setFirstName("Pesho");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void testGetAllUsersWithFilters() {
        FilterUserOptions filterUserOptions = new FilterUserOptions("John", "Doe", "johndoe", "john@example.com", "username", "asc");
        List<User> expectedUsers = Collections.singletonList(user);
        when(userRepository.getAll(filterUserOptions)).thenReturn(expectedUsers);

        List<User> result = userService.getAll(filterUserOptions);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository, times(1)).getAll(filterUserOptions);
    }

    @Test
    void testGetAllUsersWithEmptyFilters() {
        FilterUserOptions filterUserOptions = new FilterUserOptions(null, null, null, null, null, null);
        List<User> expectedUsers = Collections.singletonList(user);
        when(userRepository.getAll(filterUserOptions)).thenReturn(expectedUsers);

        List<User> result = userService.getAll(filterUserOptions);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository, times(1)).getAll(filterUserOptions);
    }

    @Test
    void testGetById() {
        when(userRepository.getById(2)).thenReturn(user);
        User result = userService.getById(admin, 2);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetByUsername() {
        when(userRepository.getByUsername("testuser")).thenReturn(user);
        User result = userService.getByUsername("testuser");
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetByUsernameForAdmin_Success() {
        when(userRepository.getByUsername("testuser")).thenReturn(user);

        User result = userService.getByUsernameForAdmin(admin, "testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).getByUsername("testuser");
    }

    @Test
    void testGetByUsernameForAdmin_UserBlocked() {
        admin.setBlocked(true);
        assertThrows(UnauthorizedOperationException.class, () -> userService.getByUsernameForAdmin(admin, "testuser"));
    }

    @Test
    void testGetByUsernameForAdmin_UserNotAdmin() {
        admin.setAdmin(false);
        assertThrows(UnauthorizedOperationException.class, () -> userService.getByUsernameForAdmin(admin, "testuser"));
    }

    @Test
    void testGetByFirstName_Success() {
        when(userRepository.getByFirstName("John")).thenReturn(user);

        User result = userService.getByFirstName(admin, "John");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).getByFirstName("John");
    }

    @Test
    void testGetByFirstName_UserBlocked() {
        admin.setBlocked(true);
        assertThrows(UnauthorizedOperationException.class, () -> userService.getByFirstName(admin, "John"));
    }

    @Test
    void testGetByFirstName_UserNotAdmin() {
        admin.setAdmin(false);
        assertThrows(UnauthorizedOperationException.class, () -> userService.getByFirstName(admin, "John"));
    }

    @Test
    void testGetByEmail() {
        when(userRepository.getByEmail("test@example.com")).thenReturn(user);
        User result = userService.getByEmail(admin, "test@example.com");
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testCreateUserThrowsDuplicateException() {
        when(userRepository.getByUsername(user.getUsername())).thenReturn(user);
        assertThrows(DuplicateEntityException.class, () -> userService.createUser(user));
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.getByUsername("testuser")).thenThrow(new EntityNotFoundException("User", "username", "testuser"));
        when(userRepository.getByEmail("test@example.com")).thenThrow(new EntityNotFoundException("User", "email", "test@example.com"));

        userService.createUser(user);

        verify(userRepository, times(1)).createUser(user);
    }

    @Test
    void testCreateUser_UsernameExists() {
        when(userRepository.getByUsername("testuser")).thenReturn(user);

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(user));
    }

    @Test
    void testCreateUser_EmailExists() {
        when(userRepository.getByUsername("testuser")).thenThrow(new EntityNotFoundException("User", "username", "testuser"));
        when(userRepository.getByEmail("test@example.com")).thenReturn(user);

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(user));
    }

    @Test
    void testUpdateUserThrowsBlockedException() {
        admin.setBlocked(true);
        UnauthorizedOperationException exception = assertThrows(UnauthorizedOperationException.class, () -> userService.updateUser(user, admin));
        assertEquals("Invalid operation. Your profile has been blocked!", exception.getMessage());
    }

    @Test
    void testUpdateUserThrowsUnauthorizedException() {
        admin.setBlocked(false);
        admin.setAdmin(false);
        UnauthorizedOperationException exception = assertThrows(UnauthorizedOperationException.class, () -> userService.updateUser(user, admin));
        assertEquals("Invalid operation. Only an Admin or Creator can modify this entity!", exception.getMessage());
    }

    @Test
    void testUpdateUserThrowsInvalidOperationException() {
        User existingUser = new User();
        existingUser.setId(2);
        existingUser.setEmail("test@example.com");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setPassword("password");

        when(userRepository.getByEmail(user.getEmail())).thenReturn(existingUser);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");

        assertThrows(InvalidOperationException.class, () -> userService.updateUser(user, admin));
    }

    @Test
    void testUpdateUserThrowsDuplicateEntityException() {
        User existingUser = new User();
        existingUser.setId(3);
        existingUser.setEmail("test@example.com");
        when(userRepository.getByEmail(user.getEmail())).thenReturn(existingUser);
        assertThrows(DuplicateEntityException.class, () -> userService.updateUser(user, admin));
    }

    @Test
    void testUpdateUserSuccess() {
        admin.setBlocked(false);
        admin.setAdmin(true);
        doThrow(new EntityNotFoundException("User", "username", user.getUsername()))
                .when(userRepository).getByEmail(user.getEmail());
        doNothing().when(userRepository).updateUser(user);
        assertDoesNotThrow(() -> userService.updateUser(user, admin));
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteUser(2);
        userService.deleteUser(admin, 2);
        verify(userRepository, times(1)).deleteUser(2);
    }

    @Test
    void testMakeAdminThrowsInvalidOperationException() {
        user.setAdmin(true);
        when(userRepository.getById(2)).thenReturn(user);
        assertThrows(InvalidOperationException.class, () -> userService.makeAdmin(admin, 2));
    }

    @Test
    void testMakeAdminSuccess() {
        when(userRepository.getById(2)).thenReturn(user);
        user.setAdmin(false);
        userService.makeAdmin(admin, 2);
        assertTrue(user.isAdmin());
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testRemoveAdminThrowsInvalidOperationException() {
        user.setAdmin(false);
        when(userRepository.getById(2)).thenReturn(user);
        assertThrows(InvalidOperationException.class, () -> userService.removeAdmin(admin, 2));
    }

    @Test
    void testRemoveAdminSuccess() {
        user.setAdmin(true);
        when(userRepository.getById(2)).thenReturn(user);
        userService.removeAdmin(admin, 2);
        assertFalse(user.isAdmin());
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testBlockUser() {
        user.setBlocked(false);
        when(userRepository.getById(2)).thenReturn(user);
        userService.blockUser(admin, 2);
        assertTrue(user.isBlocked());
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void testUnblockUser() {
        user.setBlocked(true);
        when(userRepository.getById(2)).thenReturn(user);
        userService.unblockUser(admin, 2);
        assertFalse(user.isBlocked());
        verify(userRepository, times(1)).updateUser(user);
    }
}
