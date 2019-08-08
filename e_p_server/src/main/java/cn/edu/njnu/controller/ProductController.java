package cn.edu.njnu.controller;

import cn.edu.njnu.model.Product;
import cn.edu.njnu.service.ProductService;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping("list")
    public Result<PageInfo<Product>> list(Integer currPage, Integer pageNum){
        return productService.list(currPage,pageNum);
    }

    @RequestMapping("add")
    public Result<Product> add(@RequestBody Product product){
        return productService.add(product);
    }

    @RequestMapping("del")
    public Result<Integer> del(Integer id){
        return productService.del(id);
    }

    @RequestMapping("update")
    public Result<Product> update(@RequestBody Product product){
        return productService.update(product);
    }

    @RequestMapping("get")
    public Result<Product> get(Integer id){
        return productService.get(id);
    }
}
