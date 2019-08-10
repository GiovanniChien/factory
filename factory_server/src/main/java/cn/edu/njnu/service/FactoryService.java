package cn.edu.njnu.service;

import cn.edu.njnu.client.UserClient;
import cn.edu.njnu.dao.FactoryMapper;
import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;
import cn.edu.njnu.repository.FactoryRepository;
import cn.edu.njnu.vo.FactoryVo;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FactoryService {

    @Autowired
    private FactoryMapper factoryMapper;
    @Autowired
    private FactoryRepository factoryRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Cacheable(value = "factory", key = "#id", unless = "#result == null")
    public Factory getFactoryByPrimaryKey(Integer id) {
        return factoryMapper.selectByPrimaryKey(id);
    }

    public List<FactoryVo> getAllFactory() {
        return factoryMapper.selectAll();
    }

    public Result<PageInfo<FactoryVo>> getFactoryList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<FactoryVo> factories = factoryMapper.selectAll();
        if (factories != null) {
            PageInfo<FactoryVo> pageInfo = new PageInfo<>(factories);
            return new Result<>("10000", "", pageInfo);
        }
        return new Result<>("10001", "查询失败", null);
    }

    @CachePut(value = "factory", key = "#factory.id", unless = "#result eq null")
    public Factory changeStatus(Factory factory) {
        factory.setUpdateTime(new Date());
        int i = factoryMapper.updateByPrimaryKeySelective(factory);
        Factory res = factoryMapper.selectByPrimaryKey(factory.getId());
        factoryRepository.save(res);
        return res;
    }

    @CacheEvict(value = "factory", key = "#factory.id")
    @Transactional
    public Result<Integer> delFactory(Factory factory) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("del factory");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            factory.setUpdateTime(new Date());
            factory.setFlag(1);
            int i = factoryMapper.updateByPrimaryKeySelective(factory);
            if (i != 0) {
                //同时删除与该工厂关联的用户
                Result<Integer> result = userClient.deleteByFactory(factory);
                if ("10000".equals(result.getCode())) {
                    factoryRepository.deleteById(factory.getId());
                    return new Result<>("10000", "", i);
                }
            }
            transactionManager.rollback(status);
            return new Result<>("10001", "删除失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return new Result<>("10002", "删除失败", 0);
        }
    }

    @CachePut(value = "factory", key = "#factory.id", unless = "#result eq null")
    public Factory insertFactory(Factory factory) {
        factory.setUpdateTime(new Date());
        factory.setCreateTime(new Date());
        try {
            factoryMapper.insertSelective(factory);
            factoryRepository.save(factory);
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @CachePut(value = "factory", key = "#factory.id", unless = "#result eq null")
    public Factory updateFactory(Factory factory) {
        factory.setUpdateTime(new Date());
        factoryMapper.updateByPrimaryKeySelective(factory);
        factoryRepository.save(factory);
        return factory;
    }

    public Result<Page<Factory>> searchFactoryByName(Integer page, Integer pageSize, String q) {
        try {
            String key = URLDecoder.decode(q, "UTF-8");
            //构建查询条件
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            //添加分词查询
            queryBuilder.withQuery(QueryBuilders.matchQuery("factoryName", key));
            //分页,elasticsearch从第0页开始查
            queryBuilder.withPageable(PageRequest.of(page - 1, pageSize));
            Page<Factory> factories = factoryRepository.search(queryBuilder.build());
            return new Result<>("10000", "", factories);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<Map<String, Object>> getFactoryVoById(Integer id) {
        try {
            Factory factory = factoryMapper.selectByPrimaryKey(id);
            Map<String, Object> res = new HashMap<>();
            res.put("factory", factory);
            Result<User> cRes = userClient.getUserByPrimaryKey(factory.getCreateUserid());
            Result<User> uRes = userClient.getUserByPrimaryKey(factory.getUpdateUserid());
            if ("10000".equals(cRes.getCode()) && "10000".equals(uRes.getCode())) {
                if (cRes.getData() != null && uRes.getData() != null) {
                    res.put("createUserName", cRes.getData().getUserRealName());
                    res.put("updateUserName", uRes.getData().getUserRealName());
                    return new Result<>("10000", "", res);
                }
            }
            return new Result<>("10001", "查询失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }
}
