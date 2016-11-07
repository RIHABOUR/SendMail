package com.yuriikovalchuk.service.user;

import com.yuriikovalchuk.domain.User;

public interface UserService {

    User add(User user);

    User getByEmail(String email);

}
