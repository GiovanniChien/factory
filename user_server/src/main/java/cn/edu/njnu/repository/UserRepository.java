package cn.edu.njnu.repository;

import cn.edu.njnu.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, Integer> {

    void deleteByFactoryId(Integer factoryId);

}
