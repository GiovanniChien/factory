package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductPlan implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private String planSeq;

    private Integer orderId;

    private ProductOrder productOrder;

    private Integer productId;

    private Integer planCount;

    private Date deliveryDate;

    private Date planStartDate;

    private Date planEndDate;

    private Integer planStatus;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;
}