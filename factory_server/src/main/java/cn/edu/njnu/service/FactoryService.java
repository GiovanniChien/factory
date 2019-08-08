package cn.edu.njnu.service;

import cn.edu.njnu.dao.FactoryMapper;
import cn.edu.njnu.model.Factory;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactoryService {

    @Autowired
    private FactoryMapper factoryMapper;

    @Cacheable(value = "factory", key = "#id", unless = "#result == null")
    public Factory getFactoryByPrimaryKey(Integer id) {
        return factoryMapper.selectByPrimaryKey(id);
    }

    public List<Factory> getFactoryList() {
        return factoryMapper.selectAll();
    }

}
