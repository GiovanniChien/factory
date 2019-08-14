package cn.edu.njnu.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "product", type = "doc", shards = 1, replicas = 0)
@Mapping(mappingPath = "/json/product_mapping.json")
public class Product implements Serializable{
    @Id
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