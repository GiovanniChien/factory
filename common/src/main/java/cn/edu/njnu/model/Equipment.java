package cn.edu.njnu.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "equipment", type = "doc", shards = 1, replicas = 0)
@Mapping(mappingPath = "/json/equipment_mapping.json")
public class Equipment implements Serializable {
    @Id
    private Integer id;

    private Integer flag;

    private Date createTime;

    private Integer createUserid;

    private Date updateTime;

    private Integer updateUserid;

    private String equipmentSeq;

    private String equipmentName;

    private String equipmentImgUrl;

    private Integer equipmentStatus;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;
}