package cn.edu.njnu.service;

import cn.edu.njnu.dao.UserMapper;
import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Result<Integer> deleteByFactory(Factory factory) {
        try {
            int i = userMapper.deleteByFactory(factory);
            return new Result<>("10000", "", i);
        } catch (Exception e) {
            return new Result<>("10001", "删除失败", 0);
        }
    }

    public Result<User> getUserByPrimaryKey(Integer id) {
        try {
            User user = userMapper.selectByPrimaryKey(id);
            return new Result<>("10000", "", user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10001", "查询失败", null);
        }
    }
}
