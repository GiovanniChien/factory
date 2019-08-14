package cn.edu.njnu.controller;

import cn.edu.njnu.model.ProductSchedule;
import cn.edu.njnu.service.ScheduleService;
import cn.edu.njnu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/list")
    public Result<Map<String, Object>> getAll(@RequestBody Map<String, Object> map) {
        return scheduleService.getAll((Map<String, Object>) map.get("form"));
    }

    @PostMapping("del")
    public Result<Integer> delete(@RequestBody ProductSchedule plan) {
        return scheduleService.delete(plan);
    }

    @GetMapping("schedule_seq")
    public Result<String> getPlanSeq() {
        return scheduleService.getScheduleSeq();
    }

    @PostMapping("insert")
    public Result<Integer> insert(@RequestBody ProductSchedule schedule) {
        return scheduleService.insertSchedule(schedule);
    }

    @PostMapping("update_status")
    public Result<ProductSchedule> updateStatus(@RequestBody ProductSchedule schedule) {
        return scheduleService.updateStatus(schedule);
    }

    @GetMapping("show")
    public Result<Map<String, Object>> getScheduleVoById(Integer id) {
        return scheduleService.getScheduleVoById(id);
    }

    @GetMapping("s_by_p")
    public Result<List<Map<String, Object>>> queryByPlanId(Integer planId) {
        return scheduleService.queryByPlanId(planId);
    }

}
