package com.example.b.service.impl;

import com.example.b.mapper.StudentMapper;
import com.example.b.mapper.UserMapper;
import com.example.b.pojo.Student;
import com.example.b.pojo.User;
import com.example.b.service.RegisterService;
import com.example.b.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Map<String, String> register(User user) {
        String name = user.getName();
        String password = user.getPassword();
        Integer auth = user.getAuth();
        String student = user.getStudent();
        // check if the name is used
        User tmp = userMapper.findByName(name);
        Map<String, String> res = new HashMap<>();
        if (tmp != null) {
            res.put("msg", "用户名已被占用");
            return res;
        }
        // check the password length
        if (password.length() > 6) {
            res.put("msg", "密码长度不能大于6");
            return res;
        }
        // 查询是否存在对应的学生，避免违反外键约束
        Student tmp1 = studentMapper.selectById(student);
        if (tmp1 == null) {
            res.put("msg", "关联的学号不存在");
            return res;
        }
        name = name.trim();
        // password = encoder.encode(password);
        userMapper.insertOne(new User(name, password, auth, student));
        res.put("msg", "注册成功");
        String token = TokenUtil.getToken(name);
        res.put("token", token);
        res.put("account", name);
        return res;
    }
}
