package cn.edu.njnu.controller;

import cn.edu.njnu.model.ProductPlan;
import cn.edu.njnu.service.PlanService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping("/list")
    public Result<Map<String, Object>> getAll(@RequestBody Map<String, Object> map) {
        return planService.getAll((Map<String, Object>) map.get("form"));
    }

    @PostMapping("del")
    public Result<Integer> delete(@RequestBody ProductPlan plan) {
        return planService.delete(plan);
    }

    @GetMapping("/plan_seq")
    public Result<String> getPlanSeq(){
        return planService.getPlanSeq();
    }

    @PostMapping("insert")
    public Result<Integer> insert(@RequestBody ProductPlan plan) {
        return planService.insertPlan(plan);
    }

    @GetMapping("detail")
    public Result<Map<String,Object>> getDetailsById(@RequestParam("id")Integer id){
        return planService.getDetailsById(id);
    }

    @PostMapping("update")
    public Result<Integer> update(@RequestBody ProductPlan plan) {
        return planService.update(plan);
    }

    @GetMapping("show")
    public Result<Map<String, Object>> getPlanVoById(Integer id) {
        return planService.getPlanVoById(id);
    }

    @GetMapping("all")
    public Result<List<Map<String,Object>>> getAllPlan(Integer factoryId){
        return planService.getAllPlan(factoryId);
    }

    @GetMapping("p_by_o")
    public Result<ProductPlan> queryPlanByOrderId(@RequestParam Integer orderId){
        return planService.queryPlanByOrderId(orderId);
    }

}
