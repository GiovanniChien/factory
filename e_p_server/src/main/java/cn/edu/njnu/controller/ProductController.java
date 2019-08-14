package cn.edu.njnu.controller;

import cn.edu.njnu.client.EPClient;
import cn.edu.njnu.client.UserClient;
import cn.edu.njnu.model.Product;
import cn.edu.njnu.model.User;
import cn.edu.njnu.service.ProductService;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private EPClient epClient;

    @RequestMapping("list")
    public Result<PageInfo<Product>> list(Integer currPage, Integer pageSize, Integer factoryId) {
        return productService.list(currPage, pageSize, factoryId);
    }

    @RequestMapping("get")
    public Result<Map<String, Object>> get(Integer id) {
        try {
            Product product = productService.get(id);
            Map<String, Object> res = new HashMap<>();
            res.put("product", product);
            Result<User> cRes = userClient.getUserByPrimaryKey(product.getCreateUserid());
            Result<User> uRes = userClient.getUserByPrimaryKey(product.getUpdateUserid());
            if ("10000".equals(cRes.getCode()) && "10000".equals(uRes.getCode())) {
                if (cRes.getData() != null && uRes.getData() != null) {
                    res.put("createUserName", cRes.getData().getUserRealName());
                    res.put("updateUserName", uRes.getData().getUserRealName());
                    return new Result<>("10000", "", res);
                }
            }
            return new Result<>("10001", "查询失败", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    @RequestMapping("update")
    public Result<Product> update(@RequestBody Product product){
        try{
            Product p=productService.selectByProductNum(product);
            if(p!=null&&p.getId()!=product.getId()){
                return new Result<Product>("10001", "产品编号重复", null);
            }
            else {
                Product res = productService.update(product);
                if (res != null) {
                    return new Result<Product>("10000", "", res);
                }
                return new Result<>("10001","修改失败",null);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new Result<>("10001","修改失败",null);
        }
    }

    @RequestMapping("del")
    public Result<Integer> del(@RequestBody Product product) {
        epClient.delByProductId(product.getId());
        return productService.del(product);
    }

    @RequestMapping("add")
    public Result<Product> add(@RequestBody Product product){
        try {
            Product p=productService.selectByProductNum(product);
            if(p!=null){
                return new Result<Product>("10001", "产品编号重复", null);
            }
            else {
                Product res = productService.add(product);
                if (res != null) {
                    return new Result<Product>("10000", "", res);
                }
                return new Result<Product>("10001", "添加失败", null);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new Result<Product>("10001", "添加失败", null);
        }
    }

    @RequestMapping("search")
    public Result<Page<Product>> searchByName(Integer currPage, Integer pageSize, Integer factoryId, String name) {
        return productService.searchByName(currPage, pageSize, factoryId, name);
    }

    //后加的
    @GetMapping("product")
    public Result<Product> queryOneProduct(@RequestParam("id") Integer id) {
        Product product = productService.get(id);
        if (product != null) {
            return new Result<Product>("10000", "", product);
        }
        return new Result<Product>("10001", "查找失败", null);
    }

    @GetMapping("all")
    public Result<List<Product>> getAllProductByFactoryId(@RequestParam("factoryId") Integer factoryId) {
        return productService.getAllProductByFactoryId(factoryId);
    }
}
