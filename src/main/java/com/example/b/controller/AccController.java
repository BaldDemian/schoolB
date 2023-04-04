package com.example.b.controller;

import com.example.b.mapper.AccMapper;
import com.example.b.pojo.Acc;
import com.example.b.service.LoginService;
import com.example.b.service.RegisterService;
import com.example.b.utils.TokenUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
public class AccController {
    XStream xStream = new XStream(new StaxDriver());
    @Autowired
    AccMapper accMapper;
    @Autowired
    LoginService loginService;
    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public String register(@RequestParam String userAccountXML) {
        xStream.processAnnotations(Acc.class);
        Acc acc = (Acc) xStream.fromXML(userAccountXML);
        Map<String, String> res = registerService.register(acc);
        return xStream.toXML(res);
    }
    @GetMapping("/login")
    public String login(@RequestParam String userAccountXML) {
        // 解析XML到User实例
        xStream.processAnnotations(Acc.class);
        Acc acc = (Acc) xStream.fromXML(userAccountXML);
        System.out.println(acc);
        // 接收结果
        Map<String, String> res = loginService.login(acc);
        // 将结果转换为XML返回
        return xStream.toXML(res);
    }
    @PostMapping("/checkToken")
    public String checkToken(@RequestBody Map<String, String> data) {
        String name = data.get("name");
        String token = data.get("token");
        if (name.equals(TokenUtil.parseToken(token))) {
            return "1"; // valid
        } else {
            return "0";
        }
    }
}
