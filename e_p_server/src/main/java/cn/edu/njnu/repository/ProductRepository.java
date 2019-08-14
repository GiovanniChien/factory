package cn.edu.njnu.repository;

import cn.edu.njnu.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<Product,Integer> {
}
