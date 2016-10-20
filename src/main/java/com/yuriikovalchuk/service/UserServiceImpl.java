package com.yuriikovalchuk.service;

import com.yuriikovalchuk.domain.User;
import com.yuriikovalchuk.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User add(@NonNull User user) {
        return userRepository.add(user);
    }

    @Override
    public User getByEmail(@NonNull String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public boolean deleteByMail(@NonNull String email) {
        return userRepository.deleteByMail(email);
    }

}
