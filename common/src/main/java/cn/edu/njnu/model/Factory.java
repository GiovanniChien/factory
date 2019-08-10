package cn.edu.njnu.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;
import java.util.Date;

/**
 * 工厂实体类
 *
 * @author Giovanni-Ch'ien
 */
@Data
@Document(indexName = "factory", type = "doc", shards = 1, replicas = 0)
@Mapping(mappingPath = "/json/factory_mapping.json")
public class Factory implements Serializable {
    @Id
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private String bak;

    private String factoryName;

    private String factoryImgUrl;

    private String factoryAddr;

    private String factoryUrl;

    private Integer factoryWorker;

    private Integer factoryStatus;

    private static final long serialVersionUID = 1L;

}