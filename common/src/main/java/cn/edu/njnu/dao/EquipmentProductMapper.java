package cn.edu.njnu.dao;

import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.model.EquipmentProduct;

import java.util.List;

public interface EquipmentProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EquipmentProduct record);

    int insertSelective(EquipmentProduct record);

    EquipmentProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentProduct record);

    int updateByPrimaryKey(EquipmentProduct record);

    List<EquipmentProduct> selectByEquipmentId(Integer equipmentId);

    int delByEquipmentId(Integer equipmentId);

    List<EquipmentProduct> selectByProductId(Integer productId);

    int delByProductId(Integer productId);

    //后加的
    List<Equipment> selEqByProductId(Integer productId);
}