package cn.edu.njnu.controller;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.service.FactoryService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FactoryController {

    @Autowired
    private FactoryService service;

    @GetMapping("factory")
    public Result<Factory> getFactoryByPrimaryId(Integer id) {
        Factory factory = service.getFactoryByPrimaryKey(id);
        if (factory != null) {
            return new Result<Factory>("10000", "", factory);
        }
        return new Result<Factory>("10001", "查询失败", null);
    }

    @GetMapping("list")
    public Result<List<Factory>> getFactoryList() {
        List<Factory> factoryList = service.getFactoryList();
        if (factoryList != null)
            return new Result<>("10000", "", factoryList);
        else
            return new Result<>("10001", "请求失败", null);
    }

}
