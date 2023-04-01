package com.example.b.service;

import com.example.b.pojo.User;

import java.util.Map;

public interface RegisterService {
    Map<String, String> register(User user);
}
