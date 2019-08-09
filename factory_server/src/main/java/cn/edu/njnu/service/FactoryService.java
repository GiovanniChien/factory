package cn.edu.njnu.service;

import cn.edu.njnu.dao.FactoryMapper;
import cn.edu.njnu.model.Factory;
import cn.edu.njnu.vo.FactoryVo;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FactoryService {

    @Autowired
    private FactoryMapper factoryMapper;

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
        System.out.println(new Date());
        int i = factoryMapper.updateByPrimaryKeySelective(factory);
        return factoryMapper.selectByPrimaryKey(factory.getId());
    }

}
