package com.yuriikovalchuk.repository.user;

import com.yuriikovalchuk.domain.User;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<String, User> repository = new ConcurrentHashMap<>();

    @Override
    public User add(@NonNull User user) {
        return repository.put(user.getEmail(), user);
    }

    @Override
    public User getByEmail(@NonNull String email) {
        return repository.get(email);
    }

}
