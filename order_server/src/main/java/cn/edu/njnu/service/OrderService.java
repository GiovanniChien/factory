package cn.edu.njnu.service;

import cn.edu.njnu.client.EPClient;
import cn.edu.njnu.client.UserClient;
import cn.edu.njnu.dao.ProductOrderMapper;
import cn.edu.njnu.model.Product;
import cn.edu.njnu.model.ProductOrder;
import cn.edu.njnu.vo.Result;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.beans.binding.ObjectBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private ProductOrderMapper orderMapper;

    @Autowired
    private EPClient EPClient;

    @Autowired
    private UserClient userClient;

    public Result<Map<String, Object>> getAll(Map<String, Object> form) {
        Integer page = (Integer) form.get("page");
        Integer pageSize = (Integer) form.get("pageSize");
        Map<String, Object> tmpMap = (Map<String, Object>) form.get("order");
        ProductOrder productOrder = new ProductOrder();
        productOrder.setFactoryId((Integer) tmpMap.get("factoryId"));
        productOrder.setOrderSeq((String) tmpMap.get("orderSeq"));
        productOrder.setOrderStatus(Integer.parseInt((String) tmpMap.get("orderStatus")));
        productOrder.setProductId((Integer) tmpMap.get("productId"));
        PageHelper.startPage(page, pageSize);
        List<ProductOrder> productOrders = orderMapper.selectAll(productOrder);
        if (productOrders != null) {
            PageInfo<ProductOrder> pageInfo = new PageInfo<>(productOrders);
            List<ProductOrder> list = pageInfo.getList();
            Map<String, Object> map = new HashMap<>();
            map.put("pageNum", pageInfo.getPageNum());
            map.put("pageSize", pageInfo.getPageSize());
            map.put("total", pageInfo.getTotal());
            map.put("pages", pageInfo.getPages());
            List<Map<String, Object>> orders = new ArrayList<>();
            for (ProductOrder order : list) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("order", order);
                Result<Product> productResult = EPClient.queryOneProduct(order.getProductId());
                if (productResult.getCode().equals("10000")) {
                    tmp.put("productName", productResult.getData().getProductName());
                } else {
                    tmp.put("productName", "");
                }
                orders.add(tmp);
            }
            map.put("orders", orders);
            return new Result<>("10000", "", map);
        }
        return new Result<>("10001", "查询失败", null);
    }

    public Result<List<ProductOrder>> getAllByFactoryId(Integer factoryId) {
        List<ProductOrder> list = orderMapper.selectAllByFactoryId(factoryId);
        if (list != null) {
            return new Result<>("10000", "", list);
        }
        return new Result<>("10001", "查询失败", null);
    }

    public Result<ProductOrder> updateStatus(ProductOrder order) {
        order.setUpdateTime(new Date());
        int i = orderMapper.updateByPrimaryKeySelective(order);
        if (i > 0) {
            return new Result<ProductOrder>("10000", "", order);
        }
        return new Result<ProductOrder>("10001", "操作失败", null);
    }

    public Result<Integer> delete(ProductOrder order) {
        try {
            order.setUpdateTime(new Date());
            order.setFlag(1);
            int i = orderMapper.updateByPrimaryKeySelective(order);
            if (i > 0) {
                return new Result<>("10000", "", i);
            }
            return new Result<>("10001", "删除失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "删除失败", 0);
        }
    }

    public Result<String> getOrderSeq() {
        String curOrderSeq = orderMapper.getOrderSeq();
        if (curOrderSeq != null) {
            int tmp = Integer.parseInt(curOrderSeq.substring(1));
            tmp++;
            StringBuilder res = new StringBuilder(String.valueOf(tmp));
            while (res.length() < 3) {
                res.insert(0, "0");
            }
            res.insert(0, "O");
            return new Result<>("10000", "", res.toString());
        }
        return new Result<>("10001", "获取失败", "");
    }

    public Result<Integer> insertOrder(ProductOrder order) {
        try {
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            //新插入的状态为未启动
            order.setOrderStatus(10);
            int i = orderMapper.insertSelective(order);
            if (i > 0) {
                return new Result<>("10000", "", i);
            }
            return new Result<>("10001", "插入失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "插入失败", 0);
        }
    }

    public Result<Map<String, Object>> getOrderVoById(Integer id) {
        try {
            ProductOrder order = orderMapper.selectByPrimaryKey(id);
            Map<String, Object> res = new HashMap<>();
            res.put("order", order);
            Integer createUserId = order.getCreateUserid();
            Integer updateUserId = order.getUpdateUserid();
            String createUserRealName = createUserId == null ? "" : userClient.getUserByPrimaryKey(createUserId).getData().getUserRealName();
            String updateUserRealName = updateUserId == null ? "" : userClient.getUserByPrimaryKey(updateUserId).getData().getUserRealName();
            res.put("createUserRealName", createUserRealName);
            res.put("updateUserRealName", updateUserRealName);
            Result<Product> productResult = EPClient.queryOneProduct(order.getProductId());
            res.put("productName", productResult.getData().getProductName());
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<Map<String, List<Object>>> statisticOrderStatus(Integer factoryId) {
        try {
            List<Map<String, Object>> list = orderMapper.statisticOrder(factoryId);
            Integer[] nums = new Integer[5];
            Arrays.fill(nums, 0);
            for (Map<String, Object> map : list) {
                int status = (int) map.get("status");
                int num = ((Long) map.get("num")).intValue();
                nums[status / 10 - 1] = num;
            }
            Map<String, List<Object>> res = new HashMap<>();
            List<Object> names = Arrays.asList("待接单", "已接单", "已排产", "生产中", "已完成");
            List<Object> rate = Arrays.asList(nums);
            res.put("name", names);
            res.put("value", rate);
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<Map<String, List<Object>>> statisticOrderByMonth(Integer factoryId) {
        try {
            List<Map<String, Object>> list = orderMapper.statisticOrderByMonth(factoryId);
            Integer[] nums = new Integer[12];
            Arrays.fill(nums, 0);
            for (Map<String, Object> map : list) {
                int month = (int) map.get("month");
                int num = ((Long) map.get("num")).intValue();
                nums[month - 1] = num;
            }
            Map<String, List<Object>> res = new HashMap<>();
            List<Object> months = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                months.add((i + 1) + "月");
            }
            List<Object> num = Arrays.asList(nums);
            res.put("month", months);
            res.put("num", num);
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }
}
