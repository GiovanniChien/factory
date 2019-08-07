package cn.edu.njnu.dao;

import cn.edu.njnu.model.RolePermit;

public interface RolePermitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePermit record);

    int insertSelective(RolePermit record);

    RolePermit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePermit record);

    int updateByPrimaryKey(RolePermit record);
}