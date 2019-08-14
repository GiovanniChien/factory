package cn.edu.njnu.dao;

import cn.edu.njnu.model.ProductPlan;

import java.util.List;

public interface ProductPlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductPlan record);

    int insertSelective(ProductPlan record);

    ProductPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductPlan record);

    int updateByPrimaryKey(ProductPlan record);

    List<ProductPlan> selectAll(ProductPlan plan);

    ProductPlan selectById(Integer id);

    String getPlanSeq();

    List<ProductPlan> selectAllPlan(Integer factoryId);

    ProductPlan selectByOrderId(Integer orderId);
}