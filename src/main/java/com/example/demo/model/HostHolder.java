package com.example.demo.model;


import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //多线程下，确保，用户拿到的是自己的user
    private  static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUsers() {
        return users.get();
    }

    public void setUsers(User user) {
        users.set(user);
    }
}
