package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderTrack implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private Integer scheduleId;

    private Integer planId;

    private Integer productId;

    private Integer workingCount;

    private Integer qualifiedCount;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;
}