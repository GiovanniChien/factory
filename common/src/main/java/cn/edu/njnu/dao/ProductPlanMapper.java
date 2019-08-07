package cn.edu.njnu.dao;

import cn.edu.njnu.model.ProductPlan;

public interface ProductPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductPlan record);

    int insertSelective(ProductPlan record);

    ProductPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductPlan record);

    int updateByPrimaryKey(ProductPlan record);
}