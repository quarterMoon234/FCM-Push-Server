package com.user_manager.user_management.rest_controllers;

import com.user_manager.user_management.models.Login;
import com.user_manager.user_management.models.User;
import com.user_manager.user_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1")
public class LoginApiController {

    private final UserService userService;

    public LoginApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/login")
    public ResponseEntity authenticateUser(@RequestBody Login login) {

        String email = userService.checkUserEmail(login.getEmail());

        if (email == null || email.isEmpty()) {
            return new ResponseEntity("Email dose not exist", HttpStatus.NOT_FOUND);
        }

        String hashedPassword = userService.checkUserPasswordByEmail(login.getEmail());

        if (!BCrypt.checkpw(login.getPassword(), hashedPassword)) {
            return new ResponseEntity("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUserDetailByEmail(login.getEmail());
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
