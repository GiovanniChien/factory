package cn.edu.njnu.service;

import cn.edu.njnu.dao.EquipmentMapper;
import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.repository.EquipmentRepository;
import cn.edu.njnu.vo.Result;
import org.springframework.data.domain.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentRepository equipmentRepository;


    public Result<PageInfo<Equipment>> selectAll(Integer currPage, Integer pageNum, Integer factoryId) {
        if (currPage == null) currPage = 1;
        PageHelper.startPage(currPage, pageNum);
        try {
            List<Equipment> equipments = equipmentMapper.selectByFactoryId(factoryId);
            if (equipments != null) {
                PageInfo<Equipment> pageInfo = new PageInfo<>(equipments);
                return new Result<>("10000", "操作成功", pageInfo);
            } else return new Result<>("10001", "操作失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "操作异常", null);
        }


    }

    @Cacheable(value = "equipment", key = "#id", unless = "#result == null")
    public Equipment getEquipmentById(Integer id) {
        return equipmentMapper.selectByPrimaryKey(id);

    }


    @CachePut(value = "equipment", key = "#equipment.id", unless = "#result eq null")
    public Equipment update(Equipment equipment) {

        equipment.setUpdateTime(new Date());
        equipmentMapper.updateByPrimaryKeySelective(equipment);
        equipment=equipmentMapper.selectByPrimaryKey(equipment.getId());
        equipmentRepository.save(equipment);
        return equipment;
    }


    @CacheEvict(value = "equipment", key = "#equipment.id")
    public  Result<Integer> del(Equipment equipment){
        try{
            equipment.setFlag(1);
            equipment.setUpdateTime(new Date());
            int i=equipmentMapper.updateByPrimaryKeySelective(equipment);
            if(i!=0){
                equipmentRepository.deleteById(equipment.getId());
                return new Result<>("10000","",i);
            }
            return new Result<>("10001","删除失败",0);
        } catch (Exception e){
            e.printStackTrace();
            return new Result<>("10002","删除失败",0);
        }
    }



    @CachePut(value = "equipment", key = "#equipment.id", unless = "#result eq null")
    public Equipment insert(Equipment equipment){
        equipment.setFlag(0);
        equipment.setEquipmentStatus(20);
        Date date=new Date();
        equipment.setUpdateTime(date);
        equipment.setCreateTime(date);
        try{
            equipmentMapper.insertSelective(equipment);
            equipmentRepository.save(equipment);
            return equipment;
        }catch (Exception e){
            return  null;
        }
    }

    public Result<Page<Equipment>> getEquipmentByName(Integer currPage, Integer pageSize, String name, Integer facrotyId) {
        try {
            String key = URLDecoder.decode(name, "UTF-8");
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(QueryBuilders.matchQuery("equipmentName", key));
            queryBuilder.withPageable(PageRequest.of(currPage - 1, pageSize));
            Page<Equipment> factories = equipmentRepository.search(queryBuilder.build());
            return new Result<>("10000", "查询成功", factories);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new Result<>("10001", "查询失败", null);
        }
    }

    public Equipment getEquipmentBySeq(Equipment equipment){
        return equipmentMapper.selectByEquipmentSeq(equipment);
    }

    //后加的
    public Result<Map<String, Object>> statisticEq(Integer factoryId) {
        try {
            int total, bootNum, downNum, faultNum;
            float bootRate = 0, downRate = 0, faultRate = 0;
            total = equipmentMapper.statisticsEq(factoryId, 0);
            if (total != 0) {
                bootNum = equipmentMapper.statisticsEq(factoryId, 10);
                downNum = equipmentMapper.statisticsEq(factoryId, 20);
                faultNum = equipmentMapper.statisticsEq(factoryId, 30);
                bootRate = (float) (bootNum * 1.0 / total);
                downRate = (float) (downNum * 1.0 / total);
                faultRate = (float) (faultNum * 1.0 / total);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("bootRate", bootRate);
            map.put("downRate", downRate);
            map.put("faultRate", faultRate);
            return new Result<>("10000", "", map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "获取失败", null);
        }
    }
}
