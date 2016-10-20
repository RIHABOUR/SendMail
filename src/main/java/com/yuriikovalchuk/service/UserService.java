package com.yuriikovalchuk.service;

import com.yuriikovalchuk.domain.User;

public interface UserService {

    User add(User user);

    User getByEmail(String email);

    boolean deleteByMail(String email);

}
