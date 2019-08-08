package cn.edu.njnu.service;

import cn.edu.njnu.dao.EquipmentMapper;
import cn.edu.njnu.dao.EquipmentProductMapper;
import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    EquipmentMapper equipmentMapper;

    public Result<Equipment> getEquipmentById(Integer id){
        Result<Equipment> result=new Result<>();
        try{

            Equipment equipment=equipmentMapper.selectByPrimaryKey(id);

            if(!equipment.equals(null)){
                result.setCode("10000");
                result.setMsg("查询成功");
                result.setData(equipment);
            }
            else
            {
                result.setCode("10001");
                result.setMsg("查询失败");
                result.setData(null);
            }
        }
            catch (Exception e) {
                result.setCode("10002");
                result.setMsg("该设备不存在");
                result.setData(null);
        }


         return  result;
    }

    public Result<List<Equipment>>  selectAll(){
        Result<List<Equipment>> result=new Result<>();
        try{
            List<Equipment> list=equipmentMapper.selectAll();
            result.setCode("10000");
            result.setMsg("查询成功");
            result.setData(list);
        }catch (Exception e) {
            result.setCode("10001");
            result.setMsg("查询失败");
            result.setData(null);
        }
        return  result;
    }

    public  Result<Equipment>  insert(Equipment equipment){
        Result<Equipment> result=new Result<>();

        try{
            int i=equipmentMapper.insertSelective(equipment);
            if(i>0){
                result.setCode("10000");
                result.setMsg("插入成功");
                result.setData(equipment);
            }
            else {
                result.setCode("10001");
                result.setMsg("插入失败");
                result.setData(equipment);
            }
        }catch (Exception e){
            result.setCode("10002");
            result.setMsg("插入失败");
            result.setData(equipment);
        }

        return  result;
    }


    public  Result<Equipment> deleteById(Integer id){
        Result<Equipment> result=new Result<>();
        try{
            Equipment equipment=equipmentMapper.selectByPrimaryKey(id);  //查询要删除设备信息
            if(!equipment.equals(null)){
                result.setCode("10000");
                result.setMsg("删除成功");
                result.setData(equipment);                 //             存入要删除的设备信息
            }
            else {
                result.setCode("10001");
                result.setMsg("删除失败");
                result.setData(equipment);

            }

            int i=equipmentMapper.deleteByPrimaryKey(id);
        } catch (Exception e){
            result.setCode("10002");
            result.setMsg("该设备不存在");
            result.setData(null);

        }

        return  result;

    }

    public  Result<Equipment> update(Equipment equipment){
        Result<Equipment> result=new Result<>();
        try{
            Equipment  eq=equipmentMapper.selectByPrimaryKey(equipment.getId());  //获取更新后的equipment
            if(!eq.equals(null)){                                                                //更新成功
                result.setCode("10000");
                result.setMsg("更新成功");
                result.setData(eq);                                                //存入更新后的equipment
            }
            else {
                result.setCode("10001");
                result.setMsg("更新失败");
                result.setData(equipment);                                        //存入更新前的equipment
            }
            int i=equipmentMapper.updateByPrimaryKey(equipment);
        }catch (Exception e){
            result.setCode("10002");
            result.setMsg("该设备不存在");
            result.setData(equipment);
        }
        return  result;
    }





}
