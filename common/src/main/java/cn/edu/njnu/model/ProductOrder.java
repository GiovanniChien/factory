package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductOrder implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private String orderSeq;

    private Integer orderSource;

    private Integer productId;

    private Integer productCount;

    private Date endDate;

    private Integer orderStatus;

    private Integer factoryId;

    private Integer factoryYield;

    private static final long serialVersionUID = 1L;
}