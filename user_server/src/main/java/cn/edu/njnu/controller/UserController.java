package cn.edu.njnu.controller;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;
import cn.edu.njnu.service.UserService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("del_by_fac")
    public Result<Integer> deleteByFactory(@RequestBody Factory factory) {
        return service.deleteByFactory(factory);
    }

    @GetMapping("get")
    public Result<User> getUserByPrimaryKey(@RequestParam Integer id){
        return service.getUserByPrimaryKey(id);
    }

}
