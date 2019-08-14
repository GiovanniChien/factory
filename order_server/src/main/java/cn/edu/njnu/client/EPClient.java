package cn.edu.njnu.client;

import cn.edu.njnu.model.Equipment;
import cn.edu.njnu.model.Product;
import cn.edu.njnu.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="e-p-server")
public interface EPClient {

    @GetMapping("/product/product")
    Result<Product> queryOneProduct(@RequestParam("id") Integer id);

    @GetMapping("/equipment/equipment")
    Result<Equipment> queryOneEquipment(@RequestParam("id") Integer id);

}
