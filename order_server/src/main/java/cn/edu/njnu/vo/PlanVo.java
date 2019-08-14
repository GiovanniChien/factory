package cn.edu.njnu.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlanVo {

    private Integer page;

    private Integer total;

    private Integer totalPage;

    private Integer pageSize;

    private List<Map<String, Object>> plans;
//    private User user;
//
//    private String createUserRealName;
//
//    private String updateUserRealName;
//
//    private String factoryName;

}
