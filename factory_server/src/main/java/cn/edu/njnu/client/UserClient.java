package cn.edu.njnu.client;

import cn.edu.njnu.model.Factory;
import cn.edu.njnu.model.User;
import cn.edu.njnu.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-server")
public interface UserClient {

    @PostMapping("/user/del_by_fac")
    Result<Integer> deleteByFactory(@RequestBody Factory factory);

    @GetMapping("/user/get")
    Result<User> getUserByPrimaryKey(@RequestParam Integer id);

}
