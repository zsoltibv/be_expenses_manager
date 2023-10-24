package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.UserDto;
import com.endava.expensesmanager.model.entity.User;
import com.endava.expensesmanager.model.mapper.UserMapper;
import com.endava.expensesmanager.repository.UserRepository;
import com.endava.expensesmanager.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getOrCreateUserByName(String name) {

        User existingUser = userRepository.findByName(name);

        if (existingUser != null) {
            return UserMapper.toDto(existingUser);
        } else {
            User newUser = new User();
            newUser.setName(name);
            newUser.setCreatedAt(LocalDateTime.now());
            userRepository.save(newUser);
            return UserMapper.toDto(newUser);
        }
    }
}
