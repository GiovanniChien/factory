package cn.edu.njnu.controller;

import cn.edu.njnu.client.EPClient;
import cn.edu.njnu.client.UserClient;
import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.model.EquipmentProduct;
import cn.edu.njnu.model.User;
import cn.edu.njnu.service.EquipmentService;
import cn.edu.njnu.vo.Result;
import org.springframework.data.domain.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    EquipmentService equipmentService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private EPClient epClient;

    @RequestMapping("list")
    public Result<PageInfo<Equipment>> selectAll(Integer currPage, Integer pageNum, Integer factoryId) {
        return equipmentService.selectAll(currPage, pageNum, factoryId);
    }

    @RequestMapping("getById")
    public Result<Map<String, Object>> get(Integer id) {
        try {
            Equipment equipment = equipmentService.getEquipmentById(id);
            Map<String, Object> res = new HashMap<>();
            res.put("equipment", equipment);
            Result<User> cRes = userClient.getUserByPrimaryKey(equipment.getCreateUserid());
            Result<User> uRes = userClient.getUserByPrimaryKey(equipment.getUpdateUserid());
            if ("10000".equals(cRes.getCode()) && "10000".equals(uRes.getCode())) {
                if (cRes.getData() != null && uRes.getData() != null) {
                    res.put("createUserName", cRes.getData().getUserRealName());
                    res.put("updateUserName", uRes.getData().getUserRealName());
                    return new Result<>("10000", "", res);
                }
            }
            return new Result<>("10001", "查询失败", null);
        } catch (Exception e) {
            return new Result<>("10001", "查询失败", null);

        }
    }


    @RequestMapping("update")
    public  Result<Equipment> update(@RequestBody Map<String,Object> jsonMap){
        Equipment equipment=new Equipment();
        equipment.setId((Integer) jsonMap.get("id"));
        equipment.setEquipmentName((String) jsonMap.get("equipmentName"));
        equipment.setEquipmentSeq((String) jsonMap.get("equipmentSeq"));
        equipment.setUpdateUserid((Integer) jsonMap.get("updateUserid"));
        equipment.setEquipmentImgUrl((String) jsonMap.get("equipmentImgUrl"));
        equipment.setEquipmentStatus((Integer) jsonMap.get("equipmentStatus"));
        equipment.setFactoryId((Integer) jsonMap.get("factoryId"));

        List<Map<String,Object>> data= (List<Map<String,Object>>) jsonMap.get("domains");

        try{
            Equipment e=equipmentService.getEquipmentBySeq(equipment);
            if(e!=null&&e.getId()!=equipment.getId()){
                return new Result<>("10001", "设备编号重复", null);
            }
            else{
                Equipment e1= equipmentService.update(equipment);
                epClient.update(data);
                return new Result<>("10000", "", e1);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result<>("10002","更新失败",null);
        }
    }


    @RequestMapping("add")
    public  Result<Equipment>  insert(@RequestBody Map<String,Object> jsonMap){
        Equipment equipment=new Equipment();
        equipment.setEquipmentName((String) jsonMap.get("equipmentName"));
        equipment.setEquipmentSeq((String) jsonMap.get("equipmentSeq"));
        equipment.setFactoryId((Integer) jsonMap.get("factoryId"));
        equipment.setCreateUserid((Integer) jsonMap.get("createUserid"));
        equipment.setUpdateUserid((Integer) jsonMap.get("updateUserid"));
        equipment.setEquipmentImgUrl((String) jsonMap.get("equipmentImgUrl"));
        List<EquipmentProduct> eps= (List<EquipmentProduct>) jsonMap.get("domains");

        try {
            Equipment e = equipmentService.getEquipmentBySeq(equipment);
            if (e!=null) {
                return new Result<>("10001", "设备编号重复", null);
            }
            else {
                Equipment e1 = equipmentService.insert(equipment);
                epClient.add(eps,e1.getId());
                return new Result<>("10000", "", e1);
            }
        }catch (Exception e){
            return  new Result<>("10002","添加失败",null);
        }
    }

    @RequestMapping("del")
    public Result<Integer> deleteById(@RequestBody Equipment equipment) {
        epClient.delByEquipmentId(equipment.getId());
        return equipmentService.del(equipment);
    }


    @RequestMapping("search")
    public Result<Page<Equipment>> getEquipmentByName(Integer currPage, Integer pageSize, String name, Integer id) {
        return equipmentService.getEquipmentByName(currPage, pageSize, name, id);
    }

    //后加的
    @GetMapping("equipment")
    public Result<Equipment> queryOneEquipment(@RequestParam("id") Integer id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment != null) {
            return new Result<>("10000", "", equipment);
        }
        return new Result<>("10001", "查询失败", null);
    }

    @GetMapping("statistics")
    public Result<Map<String,Object>> statisticsEq(Integer factoryId){
        return equipmentService.statisticEq(factoryId);
    }

}
