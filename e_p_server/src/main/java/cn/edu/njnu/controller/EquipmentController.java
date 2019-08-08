package cn.edu.njnu.controller;

import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.service.EquipmentService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    EquipmentService equipmentService;

    @RequestMapping ("getById/{id}")
    public Result<Equipment> getEquipmentById( @PathVariable  Integer id){
        return  equipmentService.getEquipmentById(id);
    }

    @RequestMapping("list")
    public  Result<List<Equipment>> selectAll(){
        return  equipmentService.selectAll();
    }

    @RequestMapping("insert")
    public  Result<Equipment>  insert( @RequestBody Equipment equipment){
        return  equipmentService.insert(equipment);
    }

    @RequestMapping("del/{id}")
    public Result<Equipment> deleteById(@PathVariable Integer id){
        return  equipmentService.deleteById(id);
    }

    @RequestMapping("update")
    public  Result<Equipment> update( @RequestBody Equipment equipment){
        return  equipmentService.update(equipment);
    }

}
