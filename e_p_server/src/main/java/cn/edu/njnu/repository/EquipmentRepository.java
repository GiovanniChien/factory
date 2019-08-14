package cn.edu.njnu.repository;

import cn.edu.njnu.model.Equipment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EquipmentRepository extends ElasticsearchRepository<Equipment,Integer> {
}
