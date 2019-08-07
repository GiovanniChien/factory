package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户类
 * @author Giovanni-Ch'ien
 */
@Data
public class User implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private Integer userStatus;

    private String userName;

    private String userRealName;

    private String userPasswd;

    private String userJobNum;

    private String userPhoneNum;

    private String userEmail;

    //将原来三个表修改为一个表，设置两种角色，一种超级管理员对应为0，另一种为管理员对应1
    //超级管理员可以对工厂进行增删改查，普通管理员只能对其对应的工厂的进行操作
    private Integer roleId;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;

}