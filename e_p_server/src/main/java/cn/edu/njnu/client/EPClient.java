package cn.edu.njnu.client;

import cn.edu.njnu.model.EquipmentProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "e-p-server")
public interface EPClient {

    @RequestMapping("/equipment-product/add/{equipmentId}")
    void add(@RequestBody List<EquipmentProduct> eps, @PathVariable("equipmentId") Integer equipmentId);

    @GetMapping("/equipment-product/delByEid")
    void delByEquipmentId(@RequestParam Integer equipmentId);

    @GetMapping("/equipment-product/delByPid")
    void delByProductId(@RequestParam Integer productId);

    @RequestMapping("/equipment-product/update")
    void update(@RequestBody List<Map<String, Object>> data);
}
