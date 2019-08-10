package cn.edu.njnu.repository;

import cn.edu.njnu.model.Factory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FactoryRepository extends ElasticsearchRepository<Factory, Integer> {
}
