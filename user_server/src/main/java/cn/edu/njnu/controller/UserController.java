package cn.edu.njnu.controller;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;
import cn.edu.njnu.service.UserService;
import cn.edu.njnu.vo.Result;
import cn.edu.njnu.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Result<User> getUserByPrimaryKey(@RequestParam Integer id) {
        return service.getUserByPrimaryKey(id);
    }

    @GetMapping("list")
    public Result<UserVo> getUserVoList(@RequestParam("page") Integer page
            , @RequestParam("pageSize") Integer pageSize, @RequestParam("factoryId") Integer factoryId) {
        return service.getUserVoList(page, pageSize, factoryId);
    }

    @PostMapping("insert")
    public Result<Integer> insert(@RequestBody User user) {
        return service.insertUser(user);
    }

    @PostMapping("del")
    public Result<Integer> delete(@RequestBody User user) {
        return service.delete(user);
    }

    @PostMapping("update")
    public Result<Integer> update(@RequestBody User user) {
        return service.update(user);
    }

    @GetMapping("search")//es根据用户名和真实姓名检索
    public Result<Page<User>> searchUserByName(Integer page, Integer pageSize, String q, Integer factoryId) {
        return service.searchUserByUserNameAndRealName(page, pageSize, q, factoryId);
    }

    @GetMapping("show")
    public Result<Map<String, Object>> getUserVoById(Integer id) {
        return service.getUserVoById(id);
    }

}
