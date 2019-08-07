package cn.edu.njnu.dao;

import cn.edu.njnu.model.EquipmentProduct;

public interface EquipmentProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentProduct record);

    int insertSelective(EquipmentProduct record);

    EquipmentProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentProduct record);

    int updateByPrimaryKey(EquipmentProduct record);
}