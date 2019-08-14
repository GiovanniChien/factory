package cn.edu.njnu.controller;

import cn.edu.njnu.model.ProductOrder;
import cn.edu.njnu.service.OrderService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("all")
    public Result<List<ProductOrder>> getAllByFactoryId(Integer factoryId) {
        return orderService.getAllByFactoryId(factoryId);
    }

    @PostMapping("list")
    public Result<Map<String, Object>> getAll(@RequestBody Map<String, Object> map) {
        return orderService.getAll((Map<String, Object>) map.get("form"));
    }

    @PostMapping("update_status")
    public Result<ProductOrder> updateStatus(@RequestBody ProductOrder order) {
        return orderService.updateStatus(order);
    }

    @PostMapping("del")
    public Result<Integer> delete(@RequestBody ProductOrder order) {
        return orderService.delete(order);
    }

    @GetMapping("/order_seq")
    public Result<String> getOrderSeq() {
        return orderService.getOrderSeq();
    }

    @PostMapping("insert")
    public Result<Integer> insert(@RequestBody ProductOrder order) {
        return orderService.insertOrder(order);
    }

    @GetMapping("show")
    public Result<Map<String, Object>> getOrderVoById(Integer id) {
        return orderService.getOrderVoById(id);
    }

    @GetMapping("statistic")
    public Result<Map<String,List<Object>>> statisticOrderStatus(Integer factoryId){
        return orderService.statisticOrderStatus(factoryId);
    }

    @GetMapping("statistic_month")
    public Result<Map<String,List<Object>>> statisticOrderByMonth(Integer factoryId){
        return orderService.statisticOrderByMonth(factoryId);
    }

}
