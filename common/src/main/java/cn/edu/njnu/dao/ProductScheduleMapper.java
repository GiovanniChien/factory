package cn.edu.njnu.dao;

import cn.edu.njnu.model.ProductSchedule;

import java.util.List;

public interface ProductScheduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductSchedule record);

    int insertSelective(ProductSchedule record);

    ProductSchedule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductSchedule record);

    int updateByPrimaryKey(ProductSchedule record);

    List<ProductSchedule> selectAll(ProductSchedule schedule);

    int hasRelatedSchedule(Integer planId);

    int hasRelatedUnfinishedSchedule(Integer planId);

    String getScheduleSeq();

    int hasFinishedNum(Integer planId);

    int getProduceNum(Integer planId);

    ProductSchedule selectById(Integer id);

    List<ProductSchedule> selectByPlanId(Integer planId);
}