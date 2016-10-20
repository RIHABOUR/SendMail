package com.yuriikovalchuk.repository;

import com.yuriikovalchuk.domain.User;

public interface UserRepository {

    User add(User user);

    User getByEmail(String email);

    boolean deleteByMail(String email);

}
