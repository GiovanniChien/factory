package cn.edu.njnu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 工厂类的返回视图，添加createUserName和updateUserName
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactoryVo implements Serializable {

    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserid;

    private String updateUserName;

    private String bak;

    private String factoryName;

    private String factoryImgUrl;

    private String factoryAddr;

    private String factoryUrl;

    private Integer factoryWorker;

    private Integer factoryStatus;

    private static final long serialVersionUID = 1L;

}
