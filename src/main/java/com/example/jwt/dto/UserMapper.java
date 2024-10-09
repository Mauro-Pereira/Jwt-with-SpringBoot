package com.example.jwt.dto;

import com.example.jwt.entity.User;

public class UserMapper {


    public static User userRequestToUser(UserRequest userRequest){
        User newUser = new User();
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(userRequest.getPassword());
        return newUser;
    }

    public static UserResponse userToUserResponse(User user){
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.isAdmin());

    }
    
}
