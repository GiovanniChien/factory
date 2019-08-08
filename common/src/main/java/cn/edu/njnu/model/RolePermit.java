package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RolePermit implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private Integer factoryId;

    private Integer roleId;

    private Integer permitId;

    private static final long serialVersionUID = 1L;

}