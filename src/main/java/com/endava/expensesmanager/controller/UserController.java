package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.model.dto.UserDto;
import com.endava.expensesmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
        private final UserService userService;

        public UserController(UserService userService){
            this.userService = userService;
        }

        @PostMapping("/auth/{name}")
        public ResponseEntity<UserDto> loginOrCreateUser(@PathVariable String name){

            UserDto userDto = userService.getOrCreateUserByName(name);
            return new ResponseEntity<>(userDto, HttpStatus.OK);

        }

}
