package com.stajbackend.demo.api.model;

import com.stajbackend.demo.api.controller.UserController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {
    private UserController userController = new UserController();
    @GetMapping("/")
    public int getUser(@RequestParam Integer id){
        return userController.getDummy();
    }

}
