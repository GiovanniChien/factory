package cn.edu.njnu.dao;

import cn.edu.njnu.model.OrderTrack;

public interface OrderTrackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderTrack record);

    int insertSelective(OrderTrack record);

    OrderTrack selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderTrack record);

    int updateByPrimaryKey(OrderTrack record);
}