package com.example.b.service;

import com.example.b.pojo.Acc;

import java.util.Map;

public interface LoginService {
    Map<String, String> login(Acc acc);
}
