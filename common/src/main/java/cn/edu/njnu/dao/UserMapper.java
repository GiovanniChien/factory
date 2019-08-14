package cn.edu.njnu.dao;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUserNameAndPwd(User record);

    int deleteByFactory(Factory factory);

    List<User> selectAll(Integer factoryId);

    Integer isUserNameExist(String userName);
}