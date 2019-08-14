package cn.edu.njnu.service;

import cn.edu.njnu.client.EPClient;
import cn.edu.njnu.client.UserClient;
import cn.edu.njnu.dao.ProductOrderMapper;
import cn.edu.njnu.dao.ProductPlanMapper;
import cn.edu.njnu.dao.ProductScheduleMapper;
import cn.edu.njnu.model.Product;
import cn.edu.njnu.model.ProductOrder;
import cn.edu.njnu.model.ProductPlan;
import cn.edu.njnu.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;

@Service
public class PlanService {

    @Autowired
    private ProductPlanMapper planMapper;

    @Autowired
    private ProductOrderMapper orderMapper;

    @Autowired
    private ProductScheduleMapper scheduleMapper;

    @Autowired
    private EPClient EPClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    public Result<Map<String, Object>> getAll(Map<String, Object> reqMap) {
        Integer page = (Integer) reqMap.get("page");
        Integer pageSize = (Integer) reqMap.get("pageSize");
        Map<String, Object> tmpMap = (Map<String, Object>) reqMap.get("plan");
        ProductPlan productPlan = new ProductPlan();
        productPlan.setFactoryId((Integer) tmpMap.get("factoryId"));
        productPlan.setPlanSeq((String) tmpMap.get("planSeq"));
        productPlan.setProductOrder(new ProductOrder());
        productPlan.getProductOrder().setOrderSeq((String) tmpMap.get("orderSeq"));
        System.out.println(productPlan.getProductOrder().getOrderSeq());
        productPlan.setPlanStatus(Integer.parseInt((String) tmpMap.get("planStatus")));
        productPlan.setProductId((Integer) tmpMap.get("productId"));
        PageHelper.startPage(page, pageSize);
        List<ProductPlan> productPlans = planMapper.selectAll(productPlan);
        if (productPlans != null) {
            PageInfo<ProductPlan> pageInfo = new PageInfo<>(productPlans);
            List<ProductPlan> list = pageInfo.getList();
            Map<String, Object> map = new HashMap<>();
            map.put("pageNum", pageInfo.getPageNum());
            map.put("pageSize", pageInfo.getPageSize());
            map.put("total", pageInfo.getTotal());
            map.put("pages", pageInfo.getPages());
            List<Map<String, Object>> plans = new ArrayList<>();
            for (ProductPlan plan : list) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("plan", plan);
                Result<Product> productResult = EPClient.queryOneProduct(plan.getProductId());
                tmp.put("productName", productResult.getData().getProductName());
                plans.add(tmp);
            }
            map.put("plans", plans);
            return new Result<>("10000", "", map);
        }
        return new Result<>("10001", "查询失败", null);
    }

    @Transactional
    public Result<Integer> delete(ProductPlan plan) {
        //当删除订单时，应该更新对应的订单状态为已接单,所以应该传入对应的订单id
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("del productPlan");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            plan.setUpdateTime(new Date());
            plan.setFlag(1);
            int i = planMapper.updateByPrimaryKeySelective(plan);
            if (i > 0) {
                ProductOrder order = new ProductOrder();
                order.setUpdateUserid(plan.getUpdateUserid());
                order.setUpdateTime(new Date());
                order.setId(plan.getOrderId());
                order.setOrderStatus(20);
                int j = orderMapper.updateByPrimaryKeySelective(order);
                if (j > 0)
                    return new Result<>("10000", "", i);
            }
            transactionManager.rollback(status);
            return new Result<>("10001", "删除失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return new Result<>("10002", "删除失败", 0);
        }
    }

    public Result<String> getPlanSeq() {
        String curPlanSeq = planMapper.getPlanSeq();
        if (curPlanSeq != null) {
            int tmp = Integer.parseInt(curPlanSeq.substring(1));
            tmp++;
            StringBuilder res = new StringBuilder(String.valueOf(tmp));
            while (res.length() < 3) {
                res.insert(0, "0");
            }
            res.insert(0, "P");
            return new Result<>("10000", "", res.toString());
        }
        return new Result<>("10001", "获取失败", "");
    }

    @Transactional
    public Result<Integer> insertPlan(ProductPlan plan) {
        //当插入生产计划时，应该更新对应的订单状态为生产中40
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("insert productPlan");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            plan.setCreateTime(new Date());
            plan.setUpdateTime(new Date());
            //新插入的状态为未启动
            plan.setPlanStatus(10);
            int i = planMapper.insertSelective(plan);
            if (i > 0) {
                ProductOrder order = new ProductOrder();
                order.setId(plan.getOrderId());
                order.setUpdateUserid(plan.getUpdateUserid());
                order.setUpdateTime(new Date());
                order.setOrderStatus(40);
                int j = orderMapper.updateByPrimaryKeySelective(order);
                if (j > 0)
                    return new Result<>("10000", "", i);
            }
            transactionManager.rollback(status);
            return new Result<>("10001", "插入失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return new Result<>("10002", "插入失败", 0);
        }
    }

    public Result<Map<String, Object>> getDetailsById(Integer id) {
        try {
            ProductPlan plan = planMapper.selectById(id);
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("plan", plan);
            Result<Product> productResult = EPClient.queryOneProduct(plan.getProductId());
            tmp.put("productName", productResult.getData().getProductName());
            return new Result<>("10000", "", tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<Integer> update(ProductPlan plan) {
        try {
            plan.setUpdateTime(new Date());
            int i = planMapper.updateByPrimaryKeySelective(plan);
            if (i > 0) {
                return new Result<>("10000", "", 1);
            }
            return new Result<>("10001", "更新失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10001", "更新失败", 0);
        }
    }

    public Result<Map<String, Object>> getPlanVoById(Integer id) {
        try {
            ProductPlan plan = planMapper.selectById(id);
            Map<String, Object> res = new HashMap<>();
            res.put("plan", plan);
            Integer createUserId = plan.getCreateUserid();
            Integer updateUserId = plan.getUpdateUserid();
            String createUserRealName = createUserId == null ? "" : userClient.getUserByPrimaryKey(createUserId).getData().getUserRealName();
            String updateUserRealName = updateUserId == null ? "" : userClient.getUserByPrimaryKey(updateUserId).getData().getUserRealName();
            res.put("createUserRealName", createUserRealName);
            res.put("updateUserRealName", updateUserRealName);
            Result<Product> productResult = EPClient.queryOneProduct(plan.getProductId());
            res.put("productName", productResult.getData().getProductName());
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<List<Map<String, Object>>> getAllPlan(Integer factoryId) {
        try {
            List<ProductPlan> productPlans = planMapper.selectAllPlan(factoryId);
            List<Map<String, Object>> res = new ArrayList<>();
            for (ProductPlan plan : productPlans) {
                Map<String, Object> map = new HashMap<>();
                map.put("plan", plan);
                map.put("finishedNum", scheduleMapper.hasFinishedNum(plan.getId()));
                map.put("produceNum", scheduleMapper.getProduceNum(plan.getId()));
                res.add(map);
            }
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<ProductPlan> queryPlanByOrderId(Integer orderId) {
        try {
            ProductPlan productPlan = planMapper.selectByOrderId(orderId);
            return new Result<>("10000", "", productPlan);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "数据获取失败", null);
        }
    }
}
