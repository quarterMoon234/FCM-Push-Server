package com.user_manager.user_management.rest_controllers;

import com.user_manager.user_management.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1")
public class RegisterApiController {

    private final UserService userService;

    public RegisterApiController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user/register")
    public ResponseEntity registerNewUser(@RequestParam("first_name") String first_name,
                                          @RequestParam("last_name") String last_name,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("token") String token
    ) {
        {
            if (first_name.isEmpty() || last_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                return new ResponseEntity<>("Please Complete all Fields", HttpStatus.BAD_REQUEST);
            }

            String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());

            int result = userService.registerNewUserServiceMethod(first_name, last_name, email, hashed_password, token);

            if (result != 1) {
                return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }
}