package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DailyWork implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private Integer scheduleId;

    private Integer equipmentId;

    private String equipmentSeq;

    private Date startTime;

    private Date endTime;

    private Integer workingCount;

    private Integer qualifiedCount;

    private Integer unqualifiedCout;

    private Integer completeFlag;

    private Integer factoryId;

    private String bak;

    private static final long serialVersionUID = 1L;

}