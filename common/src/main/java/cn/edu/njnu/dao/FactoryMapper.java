package cn.edu.njnu.dao;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.vo.FactoryVo;

import java.util.List;

public interface FactoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Factory record);

    int insertSelective(Factory record);

    Factory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Factory record);

    int updateByPrimaryKey(Factory record);

    List<FactoryVo> selectAll();
}