package cn.edu.njnu.controller;

import cn.edu.njnu.model.User;
import cn.edu.njnu.service.LoginService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService service;

    @PostMapping("/login-pwd")
    public Result<User> login(@RequestBody User user) {
        return service.login(user);
    }

}
