package com.yuriikovalchuk.service;

import com.yuriikovalchuk.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.yuriikovalchuk.TestData.TEST_EMAIL;
import static com.yuriikovalchuk.TestData.TEST_USER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testAdd() throws Exception {
        userService.add(TEST_USER);
        verify(userRepository).add(TEST_USER);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNull() throws Exception {
        userService.add(null);
    }

    @Test
    public void testGetByEmail() throws Exception {
        userService.getByEmail(TEST_EMAIL);
        verify(userRepository).getByEmail(TEST_EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NullPointerException.class)
    public void testGetByEmailNull() throws Exception {
        userService.getByEmail(null);
    }

    @Test
    public void testDeleteByMail() throws Exception {
        userService.deleteByMail(TEST_EMAIL);
        verify(userRepository).deleteByMail(TEST_EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByEmailNull() throws Exception {
        userService.deleteByMail(null);
    }

}