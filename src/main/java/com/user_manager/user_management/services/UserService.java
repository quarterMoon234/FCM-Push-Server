package com.user_manager.user_management.services;

import com.user_manager.user_management.models.User;
import com.user_manager.user_management.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int registerNewUserServiceMethod(String first_name, String last_name, String email, String password, String token) {
        return userRepository.registerNewUser(first_name, last_name, email, password, token);
    }

    public String checkUserEmail(String email) {
        return userRepository.checkUserEmail(email);
    }


    public String checkUserPasswordByEmail(String email) {
        return userRepository.checkUserPasswordByEmail(email);
    }

    public User getUserDetailByEmail(String email) {
        return userRepository.GetUserDetailsByEmail(email);
    }
}