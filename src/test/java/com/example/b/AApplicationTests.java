package com.example.b;

import com.example.b.pojo.User;
import com.example.b.service.LoginService;
import com.example.b.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AApplicationTests {
    @Autowired
    RegisterService registerService;
    @Autowired
    LoginService loginService;
    @Test
    void contextLoads() {
    }

    @Test
    void testDatabase() {
        registerService.register(new User("rr", "123", 2, "12"));
    }

}
