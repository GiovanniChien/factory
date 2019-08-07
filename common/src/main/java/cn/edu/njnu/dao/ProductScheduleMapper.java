package cn.edu.njnu.dao;

import cn.edu.njnu.model.ProductSchedule;

public interface ProductScheduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductSchedule record);

    int insertSelective(ProductSchedule record);

    ProductSchedule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductSchedule record);

    int updateByPrimaryKey(ProductSchedule record);
}