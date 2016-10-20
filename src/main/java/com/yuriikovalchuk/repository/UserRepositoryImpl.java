package com.yuriikovalchuk.repository;

import com.yuriikovalchuk.domain.User;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<String, User> repository = new ConcurrentHashMap<>();

    {
        add(new User("jurofreddy@gmail.com"));
    }

    @Override
    public User add(@NonNull User user) {
        return repository.put(user.getEmail(), user);
    }

    @Override
    public User getByEmail(@NonNull String email) {
        return repository.get(email);
    }

    @Override
    public boolean deleteByMail(@NonNull String email) {
        return repository.remove(email) != null;
    }

}
