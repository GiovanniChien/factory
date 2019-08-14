package cn.edu.njnu.controller;

import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.model.EquipmentProduct;
import cn.edu.njnu.service.EPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipment-product")
public class EPController {

    @Autowired
    private EPService epService;

    @RequestMapping("selByEid")
    public List<EquipmentProduct> selectByEquipmentId(Integer equipmentId){
        System.out.println(equipmentId);
        return epService.selectByEquipmentId(equipmentId);
    }

    @RequestMapping("update")
    public void update(@RequestBody List<Map<String,Object>> data){
        epService.update(data);
    }

    @RequestMapping("del")
    public void del(Integer id){
        epService.del(id);
    }

    @RequestMapping("add/{equipmentId}")
    public void add(@RequestBody List<EquipmentProduct> eps,@PathVariable("equipmentId") Integer equipmentId){
        epService.add(eps,equipmentId);
    }

    @RequestMapping("delByEid")
    public void delByEquipmentId(Integer equipmentId){
        epService.delByEquipmentId(equipmentId);
    }

    @RequestMapping("selByPid")
    public List<EquipmentProduct> selectByProductId(Integer productId){
        return epService.selectByProductId(productId);
    }

    @RequestMapping("delByPid")
    public void delByProductId(Integer productId){
        epService.delByProductId(productId);
    }

    //后加的
    @RequestMapping("e_by_p")
    public List<Equipment> selectEquipmentByProductId(Integer productId){
        return epService.selectEquipmentByProductId(productId);
    }

}
