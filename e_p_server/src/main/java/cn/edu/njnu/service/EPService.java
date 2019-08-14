package cn.edu.njnu.service;

import cn.edu.njnu.dao.EquipmentMapper;
import cn.edu.njnu.dao.EquipmentProductMapper;
import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.model.EquipmentProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EPService {

    @Autowired
    private EquipmentProductMapper epMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    public List<EquipmentProduct> selectByEquipmentId(Integer equipmentId) {
        return epMapper.selectByEquipmentId(equipmentId);
    }

    public void update(List<Map<String, Object>> data) {
        EquipmentProduct ep = new EquipmentProduct();
        for (Map<String, Object> map : data) {
            switch ((Integer) map.get("flag")) {
                case 1:
                    ep.setId(null);
                    ep.setProductId((Integer) map.get("productId"));
                    ep.setFactoryId((Integer) map.get("factoryId"));
                    ep.setYield((Integer) map.get("yield"));
                    ep.setUnit((Integer) map.get("unit"));
                    ep.setEquipmentId((Integer) map.get("equipmentId"));
                    epMapper.insertSelective(ep);
                    break;
                case 0:
                    ep.setId((Integer) map.get("id"));
                    ep.setProductId((Integer) map.get("productId"));
                    ep.setFactoryId((Integer) map.get("factoryId"));
                    ep.setYield((Integer) map.get("yield"));
                    ep.setUnit((Integer) map.get("unit"));
                    ep.setEquipmentId((Integer) map.get("equipmentId"));
                    epMapper.updateByPrimaryKeySelective(ep);
                    break;
            }
        }
    }

    public void del(Integer id) {
        epMapper.deleteByPrimaryKey(id);
    }

    public void add(List<EquipmentProduct> eps,Integer equipmentId) {
        for (EquipmentProduct ep : eps) {
            ep.setEquipmentId(equipmentId);
            epMapper.insertSelective(ep);
        }
    }

    public void delByEquipmentId(Integer equipmentId) {
        epMapper.delByEquipmentId(equipmentId);
    }

    public List<EquipmentProduct> selectByProductId(Integer productId) {
        return epMapper.selectByProductId(productId);
    }

    public void delByProductId(Integer productId) {
        epMapper.delByProductId(productId);
    }

    //后加的
    public List<Equipment> selectEquipmentByProductId(Integer productId) {
        return epMapper.selEqByProductId(productId);
    }
}
