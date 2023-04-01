package com.example.b;

import com.example.b.mapper.CourseMapper;
import com.example.b.mapper.EnrollMapper;
import com.example.b.pojo.User;
import com.example.b.service.LoginService;
import com.example.b.service.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BApplicationTests {
    @Autowired
    RegisterService registerService;
    @Autowired
    LoginService loginService;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    EnrollMapper enrollMapper;
    @Test
    void contextLoads() {
    }

    @Test
    void testDatabase() {
        registerService.register(new User("aaa", "123", 2, "123"));
    }

    @Test
    void testDatabase1() {
        System.out.println(courseMapper.selectById("11"));
    }
    @Test
    void testDatabase2() {
        enrollMapper.deleteByCnoSno("11", "123");
    }
}
