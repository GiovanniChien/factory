package cn.edu.njnu.service;

import cn.edu.njnu.dao.ProductMapper;
import cn.edu.njnu.model.Product;
import cn.edu.njnu.repository.ProductRepository;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    public Result<PageInfo<Product>> list(Integer currPage, Integer pageSize, Integer factoryId) {
        if (currPage == null) currPage = 1;
        PageHelper.startPage(currPage, pageSize);
        List<Product> products = productMapper.selectByFactoryId(factoryId);
        if (products != null) {
            PageInfo<Product> pageInfo = new PageInfo<>(products);
            return new Result<>("10000", "", pageInfo);
        }
        return new Result<>("10001", "查询失败", null);
    }

    public Product get(Integer id) {
        return productMapper.selectByPrimaryKeyWithoutFlag(id);
    }

    @CachePut(value = "product", key = "#product.id", unless = "#result eq null")
    public Product update(Product product) {
        Date date = new Date();
        product.setUpdateTime(date);
        try {
            productMapper.updateByPrimaryKeySelective(product);
            product=productMapper.selectByPrimaryKey(product.getId());
            productRepository.save(product);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @CacheEvict(value = "product", key = "#product.id")
    public Result<Integer> del(Product product) {
        product.setFlag(1);
        Date date = new Date();
        product.setUpdateTime(date);
        try {
            productMapper.updateByPrimaryKeySelective(product);
            productRepository.deleteById(product.getId());
            return new Result<>("10000", "", product.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10001", "删除失败", null);
        }
    }

    @CachePut(value = "product", key = "#product.id", unless = "#result eq null")
    public Product add(Product product) {
        Date date = new Date();
        product.setCreateTime(date);
        product.setUpdateTime(date);
        try {
            productMapper.insertSelective(product);
            productRepository.save(product);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result<Page<Product>> searchByName(Integer currPage, Integer pageSize, Integer factoryId, String name) {
        try {
            String key = URLDecoder.decode(name, "UTF-8");
            //构建查询条件
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            //添加分词查询
            queryBuilder.withQuery(QueryBuilders.matchQuery("productName", key));
            //分页,elasticsearch从第0页开始查
            queryBuilder.withPageable(PageRequest.of(currPage - 1, pageSize));
            Page<Product> factories = productRepository.search(queryBuilder.build());
            return new Result<>("10000", "", factories);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10001", "搜索失败", null);
        }
    }

    public Product selectByProductNum(Product product){
        return productMapper.selectByProductNum(product);
    }

    //后加的
    public Result<List<Product>> getAllProductByFactoryId(Integer factoryId) {
        List<Product> products = productMapper.selectByFactoryId(factoryId);
        if (products != null) {
            return new Result<>("10000", "", products);
        }
        return new Result<>("10001", "查询失败", null);
    }

}
