package cn.edu.njnu.client;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "factory-server")
public interface FactoryClient {

    @GetMapping("factory")
    Result<Factory> getFactoryByPrimaryId(@RequestParam Integer id);

}
