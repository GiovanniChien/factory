package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductSchedule implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private String scheduleSeq;

    private Integer scheduleCount;

    private Integer scheduleStatus;

    private Integer planId;

    private Integer productId;

    private Integer equipmentId;

    private Date startDate;

    private Date endDate;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;
}