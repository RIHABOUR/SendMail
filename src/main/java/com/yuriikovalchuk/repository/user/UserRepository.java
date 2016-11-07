package com.yuriikovalchuk.repository.user;

import com.yuriikovalchuk.domain.User;

public interface UserRepository {

    User add(User user);

    User getByEmail(String email);

}
