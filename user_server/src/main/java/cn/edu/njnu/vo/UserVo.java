package cn.edu.njnu.vo;

import cn.edu.njnu.model.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class UserVo {

    private Integer page;

    private Integer total;

    private Integer totalPage;

    private Integer pageSize;

    private List<Map<String,Object>> userInfo;
//    private User user;
//
//    private String createUserRealName;
//
//    private String updateUserRealName;
//
//    private String factoryName;

}
