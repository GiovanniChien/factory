package cn.edu.njnu.service;

import cn.edu.njnu.dao.UserMapper;
import cn.edu.njnu.model.User;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

    public Result<User> login(User user) {
        User u = userMapper.selectByUserNameAndPwd(user);
        Result<User> result = new Result<>();
        if (u == null) {
            result.setCode("10001");
            result.setMsg("用户名或密码不正确");
            result.setData(null);
        } else {
            result.setCode("10000");
            result.setMsg("登录成功");
            result.setData(u);
        }
        return result;
    }

}
