package com.example.b.service;

import com.example.b.pojo.User;

import java.util.Map;

public interface LoginService {
    Map<String, String> login(User user);
}
