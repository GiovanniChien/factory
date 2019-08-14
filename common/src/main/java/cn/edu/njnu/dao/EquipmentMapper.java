package cn.edu.njnu.dao;

import cn.edu.njnu.model.Equipment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquipmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Equipment record);

    int insertSelective(Equipment record);

    Equipment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Equipment record);

    int updateByPrimaryKey(Equipment record);

    List<Equipment> selectByFactoryId(Integer factoryId);

    Equipment selectByEquipmentSeq(Equipment equipment);

    /*后加*/
    int statisticsEq(@Param("factoryId") Integer factoryId, @Param("equipmentStatus") Integer equipmentStatus);

}