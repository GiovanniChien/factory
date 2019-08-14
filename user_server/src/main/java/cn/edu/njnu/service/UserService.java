package cn.edu.njnu.service;

import cn.edu.njnu.client.FactoryClient;
import cn.edu.njnu.dao.UserMapper;
import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;
import cn.edu.njnu.repository.UserRepository;
import cn.edu.njnu.vo.Result;
import cn.edu.njnu.vo.UserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FactoryClient factoryClient;

    public Result<Integer> deleteByFactory(Factory factory) {
        try {
            int i = userMapper.deleteByFactory(factory);
            userRepository.deleteByFactoryId(factory.getId());
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

    //pageHelper对多表关系查询存在问题，所以只能多次查询数据库
    public Result<UserVo> getUserVoList(Integer page, Integer pageSize, Integer factoryId) {
        PageHelper.startPage(page, pageSize);
        List<User> users = userMapper.selectAll(factoryId);
        if (users != null) {
            UserVo userVo = new UserVo();
            PageInfo<User> pageInfo = new PageInfo<>(users);
            userVo.setPage(pageInfo.getPageNum());
            userVo.setPageSize(pageInfo.getPageSize());
            userVo.setTotalPage(pageInfo.getPages());
            userVo.setTotal((int) pageInfo.getTotal());
            List<Map<String, Object>> userInfo = new ArrayList<>();
            String factoryName = "";
            for (User user : pageInfo.getList()) {
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                Integer createUserId = user.getCreateUserid();
                Integer updateUserId = user.getUpdateUserid();
                Integer fId = user.getFactoryId();
                String createUserRealName = createUserId == null ? "" : userMapper.selectByPrimaryKey(createUserId).getUserRealName();
                String updateUserRealName = updateUserId == null ? "" : userMapper.selectByPrimaryKey(updateUserId).getUserRealName();
                if ("".equals(factoryName))
                    factoryName = fId == 0 ? "" : factoryClient.getFactoryByPrimaryId(fId).getData().getFactoryName();
                map.put("createUserRealName", createUserRealName);
                map.put("updateUserRealName", updateUserRealName);
                map.put("factoryName", factoryName);
                userInfo.add(map);
            }
            userVo.setUserInfo(userInfo);
            return new Result<>("10000", "", userVo);
        }
        return new Result<>("10001", "查询失败", null);
    }

    public Result<Integer> insertUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (user.getRoleId() == 0) user.setFactoryId(0);
        try {
            if (userMapper.isUserNameExist(user.getUserName()) != null) {
                //用户名已存在，返回10001
                return new Result<>("10001", "用户名已存在", 0);
            }
            int i = userMapper.insertSelective(user);
            if (i > 0) {
                userRepository.save(user);
            }
            return new Result<>("10000", "", i);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "插入失败", 0);
        }
    }

    public Result<Integer> delete(User user) {
        try {
            user.setUpdateTime(new Date());
            user.setFlag(1);
            int i = userMapper.updateByPrimaryKeySelective(user);
            if (i > 0) {
                userRepository.deleteById(user.getId());
                return new Result<>("10000", "", i);
            }
            return new Result<>("10001", "删除失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10001", "删除失败", 0);
        }
    }


    public Result<Integer> update(User user) {
        try {
            user.setUpdateTime(new Date());
            if (user.getRoleId() == 0) user.setFactoryId(0);
            Integer isEx = userMapper.isUserNameExist(user.getUserName());
            if (isEx != null && !isEx.equals(user.getId())) {
                //用户名已存在，返回10001
                return new Result<>("10001", "用户名已存在", 0);
            }
            int i = userMapper.updateByPrimaryKeySelective(user);
            if (i > 0) {
                userRepository.save(user);
                return new Result<>("10000", "", 1);
            }
            return new Result<>("10001", "更新失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10001", "更新失败", 0);
        }
    }

    //es多条件查询
    public Result<Page<User>> searchUserByUserNameAndRealName(Integer page, Integer pageSize, String q, Integer factoryId) {
        try {
            String key = URLDecoder.decode(q, "UTF-8");
            //构建查询条件
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            //添加分词查询
            //多个查询条件
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //must表示该字段必须满足此条件,
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(q, "userName", "userRealName"));
            //多个must可以继续添加
            //should表示应该满足，可以不满足
            boolQueryBuilder.should(QueryBuilders.termQuery("factoryId", factoryId));
            boolQueryBuilder.should(QueryBuilders.termQuery("roleId", 0));
            //miniShouldMatch设置should中至少应该满足多少个条件
            boolQueryBuilder.minimumShouldMatch(1);
            queryBuilder.withQuery(boolQueryBuilder);
            //分页,elasticsearch从第0页开始查
            queryBuilder.withPageable(PageRequest.of(page - 1, pageSize));
            Page<User> users = userRepository.search(queryBuilder.build());
            return new Result<>("10000", "", users);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<Map<String, Object>> getUserVoById(Integer id) {
        try {
            User user = userMapper.selectByPrimaryKey(id);
            Map<String, Object> res = new HashMap<>();
            res.put("user", user);
            Integer createUserId = user.getCreateUserid();
            Integer updateUserId = user.getUpdateUserid();
            Integer fId = user.getFactoryId();
            String createUserRealName = createUserId == null ? "" : userMapper.selectByPrimaryKey(createUserId).getUserRealName();
            String updateUserRealName = updateUserId == null ? "" : userMapper.selectByPrimaryKey(updateUserId).getUserRealName();
            String factoryName = fId == 0 ? "" : factoryClient.getFactoryByPrimaryId(fId).getData().getFactoryName();
            res.put("createUserRealName", createUserRealName);
            res.put("updateUserRealName", updateUserRealName);
            res.put("factoryName", factoryName);
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }
}
