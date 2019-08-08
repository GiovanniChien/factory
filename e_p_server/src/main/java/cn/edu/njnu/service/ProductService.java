package cn.edu.njnu.service;

import cn.edu.njnu.dao.ProductMapper;
import cn.edu.njnu.model.Product;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductMapper productMapper;

    public Result<PageInfo<Product>> list(Integer currPage,Integer pageNum){
        if(currPage==null) currPage=1;
        PageHelper.startPage(currPage, pageNum);
        List<Product> products = productMapper.selectAll();
        PageInfo<Product> pageInfo=new PageInfo<>(products);

        Result<PageInfo<Product>> result=new Result<>();
        result.setCode("10000");
        result.setMsg("加载成功");
        result.setData(pageInfo);
        return result;
    }

    public Result<Product> add(Product product){
        Date date=new Date();
        product.setCreateTime(date);
        product.setUpdateTime(date);
        int i=productMapper.insertSelective(product);
        Result<Product> result=new Result<>();
        if(i!=0){
            result.setCode("10000");
            result.setMsg("添加成功");
            result.setData(product);
        }
        else{
            result.setCode("10001");
            result.setMsg("添加失败");
            result.setData(product);
        }
        return result;
    }

    public Result<Integer> del(Integer id){
        int i=productMapper.deleteByPrimaryKey(id);
        Result<Integer> result=new Result<>();
        if(i!=0){
            result.setCode("10000");
            result.setMsg("删除成功");
            result.setData(id);
        }
        else{
            result.setCode("10001");
            result.setMsg("删除失败");
            result.setData(id);
        }
        return result;
    }

    public Result<Product> update(Product product){
        Date date=new Date();
        product.setUpdateTime(date);
        int i=productMapper.updateByPrimaryKeySelective(product);
        Result<Product> result=new Result<>();
        if(i!=0){
            result.setCode("10000");
            result.setMsg("修改成功");
            result.setData(product);
        }
        else{
            result.setCode("10001");
            result.setMsg("修改失败");
            result.setData(product);
        }
        return result;
    }

    public Result<Product> get(Integer id){
        Product product = productMapper.selectByPrimaryKey(id);
        Result<Product> result=new Result<>();
        if(product!=null){
            result.setCode("10000");
            result.setMsg("获取详情成功");
            result.setData(product);
        }
        else{
            result.setCode("10001");
            result.setMsg("获取详情失败");
            result.setData(product);
        }
        return result;
    }
}
