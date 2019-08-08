package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Product implements Serializable {
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private String productNum;

    private String productName;

    private String productImgUrl;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;

}