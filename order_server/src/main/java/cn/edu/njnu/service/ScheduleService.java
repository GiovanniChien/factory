package cn.edu.njnu.service;

import cn.edu.njnu.client.EPClient;
import cn.edu.njnu.client.UserClient;
import cn.edu.njnu.dao.ProductOrderMapper;
import cn.edu.njnu.dao.ProductPlanMapper;
import cn.edu.njnu.dao.ProductScheduleMapper;
import cn.edu.njnu.model.*;
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
public class ScheduleService {

    @Autowired
    private ProductScheduleMapper scheduleMapper;

    @Autowired
    private ProductPlanMapper planMapper;

    @Autowired
    private ProductOrderMapper orderMapper;

    @Autowired
    private EPClient EPClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    public Result<Map<String, Object>> getAll(Map<String, Object> form) {
        Integer page = (Integer) form.get("page");
        Integer pageSize = (Integer) form.get("pageSize");
        Map<String, Object> tmpMap = (Map<String, Object>) form.get("schedule");
        ProductSchedule productSchedule = new ProductSchedule();
        productSchedule.setFactoryId((Integer) tmpMap.get("factoryId"));
        productSchedule.setScheduleSeq((String) tmpMap.get("scheduleSeq"));
        productSchedule.setProductPlan(new ProductPlan());
        productSchedule.getProductPlan().setPlanSeq((String) tmpMap.get("planSeq"));
        productSchedule.setProductId((Integer) tmpMap.get("productId"));
        PageHelper.startPage(page, pageSize);
        List<ProductSchedule> productSchedules = scheduleMapper.selectAll(productSchedule);
        if (productSchedules != null) {
            PageInfo<ProductSchedule> pageInfo = new PageInfo<>(productSchedules);
            List<ProductSchedule> list = pageInfo.getList();
            Map<String, Object> map = new HashMap<>();
            map.put("pageNum", pageInfo.getPageNum());
            map.put("pageSize", pageInfo.getPageSize());
            map.put("total", pageInfo.getTotal());
            map.put("pages", pageInfo.getPages());
            List<Map<String, Object>> schedules = new ArrayList<>();
            for (ProductSchedule schedule : list) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("schedule", schedule);
                Result<Product> productResult = EPClient.queryOneProduct(schedule.getProductId());
                tmp.put("productName", productResult.getData().getProductName());
                Result<Equipment> equipmentResult = EPClient.queryOneEquipment(schedule.getEquipmentId());
                tmp.put("equipmentSeq", equipmentResult.getData().getEquipmentSeq());
                schedules.add(tmp);
            }
            map.put("schedules", schedules);
            return new Result<>("10000", "", map);
        }
        return new Result<>("10001", "查询失败", null);
    }

    @Transactional
    public Result<Integer> delete(ProductSchedule schedule) {
        //前端应该传来对应的planid
        //当删除调度单时，存在两种情况：1.对应的订单还存在其他的调度单，2.对应的订单不存在其他的调度单
        //若是第一种情况，则无需更新调度单，若是第二种情况则需要更新对应的计划为未开始
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("del productSchedule");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            schedule.setUpdateTime(new Date());
            schedule.setFlag(1);
            int i = scheduleMapper.updateByPrimaryKeySelective(schedule);
            if (i > 0) {
                int exist = scheduleMapper.hasRelatedSchedule(schedule.getPlanId());
                if (exist == 0) {   //不存在其他的调度
                    ProductPlan plan = new ProductPlan();
                    plan.setUpdateUserid(schedule.getUpdateUserid());
                    plan.setUpdateTime(schedule.getUpdateTime());
                    plan.setId(schedule.getPlanId());
                    plan.setPlanStatus(10);
                    int j = planMapper.updateByPrimaryKeySelective(plan);
                    if (j > 0)
                        return new Result<>("10000", "", i);
                } else {
                    //存在其他相关的调度，无需更新
                    return new Result<>("10000", "", i);
                }
            }
            transactionManager.rollback(status);
            return new Result<>("10001", "删除失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return new Result<>("10002", "删除失败", 0);
        }
    }

    public Result<String> getScheduleSeq() {
        String curScheduleSeq = scheduleMapper.getScheduleSeq();
        if (curScheduleSeq != null) {
            int tmp = Integer.parseInt(curScheduleSeq.substring(1));
            tmp++;
            StringBuilder res = new StringBuilder(String.valueOf(tmp));
            while (res.length() < 3) {
                res.insert(0, "0");
            }
            res.insert(0, "S");
            return new Result<>("10000", "", res.toString());
        }
        return new Result<>("10001", "获取失败", "");
    }

    @Transactional
    public Result<Integer> insertSchedule(ProductSchedule schedule) {
        //当产生新的调度时，若是对应计划的第一个调度，则应该更新这个计划为20（已启动）
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("insert productSchedule");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            //新插入的状态为未开始
            schedule.setScheduleStatus(10);
            int i = scheduleMapper.insertSelective(schedule);
            if (i > 0) {
                int exist = scheduleMapper.hasRelatedSchedule(schedule.getPlanId());
                if (exist == 1) {   //插入的调度时第一个，则更新计划状态
                    ProductPlan plan = new ProductPlan();
                    plan.setUpdateUserid(schedule.getUpdateUserid());
                    plan.setUpdateTime(schedule.getUpdateTime());
                    plan.setId(schedule.getPlanId());
                    plan.setPlanStatus(20);
                    int j = planMapper.updateByPrimaryKeySelective(plan);
                    if (j > 0)
                        return new Result<>("10000", "", i);
                } else {
                    //存在其他相关的调度，无需更新
                    return new Result<>("10000", "", i);
                }
            }
            transactionManager.rollback(status);
            return new Result<>("10001", "插入失败", 0);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return new Result<>("10002", "插入失败", 0);
        }
    }

    @Transactional
    public Result<ProductSchedule> updateStatus(ProductSchedule schedule) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("update scheduleStatus");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            schedule.setUpdateTime(new Date());
            if (schedule.getScheduleStatus() == 20) {//开始这个调度
                schedule.setStartDate(new Date());
                int i = scheduleMapper.updateByPrimaryKeySelective(schedule);
                return new Result<>("10000", "", schedule);
            } else {//完成这个调度
                schedule.setEndDate(new Date());
                int i = scheduleMapper.updateByPrimaryKeySelective(schedule);
                //当前调度完成后，有如下情况:若已经没有其他相关的未完成的调度，且生产数量大于等于计划数量
                int exitUnfinishedSchedule = scheduleMapper.hasRelatedUnfinishedSchedule(schedule.getPlanId());
                if (exitUnfinishedSchedule == 0) {  //没有其他未完成的相关调度
                    //获取已加工的产品数量
                    int finishedNum = scheduleMapper.hasFinishedNum(schedule.getPlanId());
                    //获取计划中总计划的产品数量
                    ProductPlan plan = planMapper.selectByPrimaryKey(schedule.getPlanId());
                    if (plan.getPlanCount() <= finishedNum) {
                        //此时该订单已完成，向上更新计划表和订单表
                        plan.setPlanStatus(30);
                        plan.setUpdateUserid(schedule.getUpdateUserid());
                        plan.setUpdateTime(schedule.getUpdateTime());
                        planMapper.updateByPrimaryKeySelective(plan);
                        ProductOrder order = new ProductOrder();
                        order.setId(plan.getOrderId());
                        order.setOrderStatus(50);
                        order.setUpdateUserid(schedule.getUpdateUserid());
                        order.setUpdateTime(schedule.getUpdateTime());
                        orderMapper.updateByPrimaryKeySelective(order);
                    }
                }
                return new Result<>("10000", "", schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return new Result<>("10002", "更新失败", null);
        }
    }

    public Result<Map<String, Object>> getScheduleVoById(Integer id) {
        try {
            ProductSchedule schedule = scheduleMapper.selectById(id);
            Map<String, Object> res = new HashMap<>();
            res.put("schedule", schedule);
            Integer createUserId = schedule.getCreateUserid();
            Integer updateUserId = schedule.getUpdateUserid();
            String createUserRealName = createUserId == null ? "" : userClient.getUserByPrimaryKey(createUserId).getData().getUserRealName();
            String updateUserRealName = updateUserId == null ? "" : userClient.getUserByPrimaryKey(updateUserId).getData().getUserRealName();
            res.put("createUserRealName", createUserRealName);
            res.put("updateUserRealName", updateUserRealName);
            Result<Product> productResult = EPClient.queryOneProduct(schedule.getProductId());
            Result<Equipment> equipmentResult = EPClient.queryOneEquipment(schedule.getEquipmentId());
            res.put("productName", productResult.getData().getProductName());
            res.put("equipmentSeq", equipmentResult.getData().getEquipmentSeq());
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "查询失败", null);
        }
    }

    public Result<List<Map<String, Object>>> queryByPlanId(Integer planId) {
        try {
            List<ProductSchedule> productSchedules = scheduleMapper.selectByPlanId(planId);
            List<Map<String, Object>> res = new ArrayList<>();
            for (ProductSchedule schedule : productSchedules) {
                Map<String, Object> map = new HashMap<>();
                map.put("schedule", schedule);
                map.put("equipmentSeq", EPClient.queryOneEquipment(schedule.getEquipmentId()).getData().getEquipmentSeq());
                res.add(map);
            }
            return new Result<>("10000", "", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>("10002", "获取失败", null);
        }
    }
}
