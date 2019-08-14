package cn.edu.njnu.dao;

import cn.edu.njnu.model.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByFactoryId(Integer factoryId);

    Product selectByProductNum(Product product);

    Product selectByPrimaryKeyWithoutFlag(Integer id);
}