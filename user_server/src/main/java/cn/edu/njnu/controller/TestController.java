package cn.edu.njnu.controller;

import cn.edu.njnu.dao.UserMapper;
import cn.edu.njnu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("test")
    public User helloWorld() {
        return userMapper.selectByPrimaryKey(1);
    }

}
