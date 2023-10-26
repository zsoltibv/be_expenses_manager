package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.UserDto;

public interface UserService {
    UserDto getOrCreateUserByName(String name);

}
