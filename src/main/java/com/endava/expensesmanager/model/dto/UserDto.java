package com.endava.expensesmanager.model.dto;

public class UserDto {

    private Integer userId;
    private String name;

    public Integer getId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer userId) {
        this.userId = userId;
    }

}
