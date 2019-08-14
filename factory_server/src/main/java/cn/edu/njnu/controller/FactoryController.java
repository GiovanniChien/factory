package cn.edu.njnu.controller;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.service.FactoryService;
import cn.edu.njnu.vo.FactoryVo;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FactoryController {

    @Autowired
    private FactoryService service;

    @GetMapping("factory")
    public Result<Factory> getFactoryByPrimaryId(@RequestParam Integer id) {
        Factory factory = service.getFactoryByPrimaryKey(id);
        if (factory != null) {
            return new Result<Factory>("10000", "", factory);
        }
        return new Result<Factory>("10001", "查询失败", null);
    }

    @GetMapping("all")
    public Result<List<FactoryVo>> getAllFactory() {
        List<FactoryVo> factoryList = service.getAllFactory();
        if (factoryList != null)
            return new Result<>("10000", "", factoryList);
        else
            return new Result<>("10001", "请求失败", null);
    }

    @GetMapping("list")
    public Result<PageInfo<FactoryVo>> getFactoryList(Integer page, Integer pageSize) {
        return service.getFactoryList(page, pageSize);
    }

    @PostMapping("update_status")
    public Result<Factory> updateStatus(@RequestBody Factory factory) {
        Factory res = service.changeStatus(factory);
        if (res != null) {
            return new Result<Factory>("10000", "", res);
        }
        return new Result<Factory>("10001", "查询失败", null);
    }

    @PostMapping("del")
    public Result<Integer> deleteFactoryByPrimaryKey(@RequestBody Factory factory) {
        return service.delFactory(factory);
    }

    @PostMapping("insert")
    public Result<Factory> insertFactory(@RequestBody Factory factory) {
        Factory res = service.insertFactory(factory);
        if (res != null) {
            return new Result<Factory>("10000", "", res);
        }
        return new Result<Factory>("10001", "插入失败", null);
    }

    @PostMapping("update")
    public Result<Factory> updateFactory(@RequestBody Factory factory) {
        Factory res = service.updateFactory(factory);
        if (res != null) {
            return new Result<Factory>("10000", "", res);
        }
        return new Result<Factory>("10001", "更新失败", null);
    }

    @GetMapping("search")//es根据工厂名检索
    public Result<Page<Factory>> searchFactoryByName(Integer page, Integer pageSize, String q) {
        return service.searchFactoryByName(page, pageSize, q);
    }

    @GetMapping("show")
    public Result<Map<String, Object>> getFactoryVoById(Integer id) {
        return service.getFactoryVoById(id);
    }

}
