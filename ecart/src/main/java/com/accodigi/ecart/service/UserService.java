package com.accodigi.ecart.service;

import com.accodigi.ecart.exception.UserNotFoundException;
import com.accodigi.ecart.model.User;
import com.accodigi.ecart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(Long userId) throws UserNotFoundException {

        final Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return user.get();
    }

}
