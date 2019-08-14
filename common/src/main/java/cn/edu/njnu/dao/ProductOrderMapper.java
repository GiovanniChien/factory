package cn.edu.njnu.dao;

import cn.edu.njnu.model.ProductOrder;

import java.util.List;
import java.util.Map;

public interface ProductOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductOrder record);

    int insertSelective(ProductOrder record);

    ProductOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductOrder record);

    int updateByPrimaryKey(ProductOrder record);

    List<ProductOrder> selectAllByFactoryId(Integer factoryId);

    List<ProductOrder> selectAll(ProductOrder productOrder);

    String getOrderSeq();

    List<Map<String,Object>> statisticOrder(Integer factoryId);

    List<Map<String,Object>> statisticOrderByMonth(Integer factoryId);
}