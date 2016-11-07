package com.yuriikovalchuk.service.user;

import com.yuriikovalchuk.domain.User;
import com.yuriikovalchuk.repository.user.UserRepository;
import com.yuriikovalchuk.util.exception.UserNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User add(@NonNull User user) {
        return userRepository.add(user);
    }

    @Override
    public User getByEmail(@NonNull String email) throws UserNotFoundException {

        User user = userRepository.getByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User with email: '" + email + "' is not authorized.");
        }

        return user;
    }

}
