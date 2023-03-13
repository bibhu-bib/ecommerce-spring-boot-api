package com.accodigi.ecart.service;

import com.accodigi.ecart.exception.UserNotFoundException;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    final Long TEST_USER_ID = 1L;
    UserService service;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository);
    }

    @Test
    void testGetUser_ShouldReturnUser() throws UserNotFoundException {
        User expectedUser = new User();
        expectedUser.setId(TEST_USER_ID);
        Mockito.when(userRepository.findById(TEST_USER_ID))
                .thenReturn(Optional.of(expectedUser));

        User actualUser = service.getUser(TEST_USER_ID);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testGetUser_WhenUserDoesNotExist_ShouldThrowException() {
        Mockito.when(userRepository.findById(TEST_USER_ID))
                .thenReturn(Optional.empty());

//        Assertions.assertThrows(UserNotFoundException.class, () -> {
//            userService.getUserById(1234);
//        });

        assertThrows(UserNotFoundException.class, () ->
                service.getUser(TEST_USER_ID));
    }

}