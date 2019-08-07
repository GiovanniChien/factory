package cn.edu.njnu.dao;

import cn.edu.njnu.model.DailyWork;

public interface DailyWorkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DailyWork record);

    int insertSelective(DailyWork record);

    DailyWork selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DailyWork record);

    int updateByPrimaryKey(DailyWork record);
}