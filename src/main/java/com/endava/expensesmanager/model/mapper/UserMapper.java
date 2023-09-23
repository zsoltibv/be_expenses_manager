package com.endava.expensesmanager.model.mapper;

import com.endava.expensesmanager.model.dto.UserDto;
import com.endava.expensesmanager.model.entity.User;

public class UserMapper {

    public static UserDto toDto(User user)
    {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static User toUser(UserDto userDto)
    {
        User user = new User();
        user.setName(userDto.getName());
        return user;
    }
}
