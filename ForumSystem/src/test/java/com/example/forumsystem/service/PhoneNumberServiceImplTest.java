package com.example.forumsystem.service;

import com.example.forumsystem.exceptions.DuplicateEntityException;
import com.example.forumsystem.exceptions.EntityNotFoundException;
import com.example.forumsystem.exceptions.InvalidOperationException;
import com.example.forumsystem.exceptions.UnauthorizedOperationException;
import com.example.forumsystem.models.Comment;
import com.example.forumsystem.models.PhoneNumber;
import com.example.forumsystem.models.User;
import com.example.forumsystem.repository.PhoneNumberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.forumsystem.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberServiceImplTest {


    @Mock
    PhoneNumberRepository phoneNumberRepository;

    @InjectMocks
    PhoneNumberServiceImpl phoneNumberService;

    @Test
    public void getAll_ShouldReturnAllPhoneNumbers_WhenValid() {
        List<PhoneNumber> mockComments = List.of(createMockPhoneNumber(), createMockPhoneNumber());

        when(phoneNumberRepository.getAll()).thenReturn(mockComments);

        List<PhoneNumber> result = phoneNumberService.getAll();

        assertEquals(mockComments, result);
    }

    @Test
    public void getById_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.getByUserId(mockUser);
        });
    }

    @Test
    public void getById_ShouldThrowException_WhenUserIsNotAnAdmin() {
        User mockUser = createMockUser();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.getByUserId(mockUser);
        });
    }

    @Test
    public void getById_ShouldReturnPhoneNumber_WhenValid() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);

        assertDoesNotThrow(() -> {
            phoneNumberService.getByUserId(mockUser);
        });
    }

    @Test
    public void getByPhoneNumber_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.getByPhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void getByPhoneNumber_ShouldThrowException_WhenUserIsNotAnAdmin() {
        User mockUser = createMockUser();
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.getByPhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void getByPhoneNumber_ShouldReturnPhoneNumber_WhenValid() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        Assertions.assertDoesNotThrow(() -> {
            phoneNumberService.getByPhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void createPhoneNumber_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.createPhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void createPhoneNumber_ShouldThrowException_WhenUserIsNotAnAdmin() {
        User mockUser = createMockUser();
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.createPhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void createPhoneNumber_ShouldThrowException_WhenUserAlreadyHasAPhoneNumber() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();
        mockPhoneNumber.setCreatedBy(mockUser);
        PhoneNumber anotherMockPhoneNumber = createMockPhoneNumber();

        assertThrows(InvalidOperationException.class, () -> {
            phoneNumberService.createPhoneNumber(mockUser, anotherMockPhoneNumber);
        });
    }

    @Test
    public void createPhoneNumber_ShouldThrowException_WhenAnotherUserAlreadyHasTheSamePhoneNumber() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        User anotherMockUser = createAnotherMockUser();
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();
        mockPhoneNumber.setCreatedBy(anotherMockUser);
        PhoneNumber anotherMockPhoneNumber = createMockPhoneNumber();

        when(phoneNumberRepository.getByUserId(mockUser)).thenThrow(new EntityNotFoundException());

        assertThrows(DuplicateEntityException.class, () -> {
            phoneNumberService.createPhoneNumber(mockUser, anotherMockPhoneNumber);
        });
    }

    @Test
    public void createPhoneNumber_ShouldCallRepo_WhenValid() {
        User mockUser = createAnotherMockUser();
        mockUser.setAdmin(true);
        PhoneNumber mockPhoneNumber = createAnotherMockPhoneNumber();

        when(phoneNumberRepository.getByUserId(mockUser)).thenThrow(new EntityNotFoundException());
        when(phoneNumberRepository.getByPhoneNumber(mockPhoneNumber)).thenThrow(new EntityNotFoundException());

        assertDoesNotThrow(() -> {
            phoneNumberService.createPhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void updatePhoneNumber_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.updatePhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void updatePhoneNumber_ShouldThrowException_WhenUserIsNotAnAdmin() {
        User mockUser = createMockUser();
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.updatePhoneNumber(mockUser, mockPhoneNumber);
        });
    }

    @Test
    public void updatePhoneNumber_ShouldThrowException_WhenPhoneNumberIsUnchanged() {
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();
        PhoneNumber anotherMockPhoneNumber = createMockPhoneNumber();

        when(phoneNumberRepository.getByUserId(mockPhoneNumber.getCreatedBy())).thenReturn(mockPhoneNumber);

        assertThrows(InvalidOperationException.class, () -> {
            phoneNumberService.updatePhoneNumber(mockPhoneNumber.getCreatedBy(), anotherMockPhoneNumber);
        });
    }

    @Test
    public void updatePhoneNumber_ShouldThrowException_WhenPhoneNumberAlreadyExists() {
        PhoneNumber newMockPhoneNumber = createMockPhoneNumber();
        newMockPhoneNumber.setPhoneNumber("0888888887");
        PhoneNumber oldMockPhoneNumber = createMockPhoneNumber();
        PhoneNumber anotherPhoneNumber = createMockPhoneNumber();
        anotherPhoneNumber.setPhoneNumber("0888888887");
        User mockUser = createMockUser();
        anotherPhoneNumber.setCreatedBy(mockUser);

        when(phoneNumberRepository.getByUserId(oldMockPhoneNumber.getCreatedBy())).thenReturn(oldMockPhoneNumber);

        assertThrows(DuplicateEntityException.class, () -> {
            phoneNumberService.updatePhoneNumber(newMockPhoneNumber.getCreatedBy(), newMockPhoneNumber);
        });

    }

    @Test
    public void updatePhoneNumber_ShouldCallRepo_WhenValid() {
        PhoneNumber newMockPhoneNumber = createMockPhoneNumber();
        newMockPhoneNumber.setPhoneNumber("0888888887");
        PhoneNumber oldMockPhoneNumber = createMockPhoneNumber();

        when(phoneNumberRepository.getByUserId(oldMockPhoneNumber.getCreatedBy())).thenReturn(oldMockPhoneNumber);
        when(phoneNumberRepository.getByPhoneNumber(newMockPhoneNumber)).thenThrow(new EntityNotFoundException());

        assertDoesNotThrow(() -> {
            phoneNumberService.updatePhoneNumber(newMockPhoneNumber.getCreatedBy(), newMockPhoneNumber);
        });
    }

    @Test
    public void deletePhoneNumber_ShouldThrowException_WhenUserIsBlocked() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.deletePhoneNumber(mockUser);
        });
    }

    @Test
    public void deletePhoneNumber_ShouldThrowException_WhenUserIsNotAnAdmin() {
        User mockUser = createMockUser();

        assertThrows(UnauthorizedOperationException.class, () -> {
            phoneNumberService.deletePhoneNumber(mockUser);
        });
    }

    @Test
    public void deletePhoneNumber_ShouldCallRepo_WhenValid() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        when(phoneNumberRepository.getByUserId(mockUser)).thenReturn(mockPhoneNumber);

        assertDoesNotThrow(() -> {
            phoneNumberService.deletePhoneNumber(mockUser);
        });
    }
}
