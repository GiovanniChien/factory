package cn.edu.njnu.client;

import cn.edu.njnu.model.User;
import cn.edu.njnu.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-server")
public interface UserClient {

    @GetMapping("/user/get")
    Result<User> getUserByPrimaryKey(@RequestParam Integer id);

}