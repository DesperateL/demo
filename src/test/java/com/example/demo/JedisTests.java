package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.util.JedisAdapter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    User user;

    @Test
    public void testObject(){
        user = new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("password");
        user.setSalt("salt");

        jedisAdapter.setObject("user1xx",user);
    }
}
