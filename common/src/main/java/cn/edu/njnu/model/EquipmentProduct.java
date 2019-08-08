package cn.edu.njnu.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class EquipmentProduct implements Serializable {
    private Integer id;

    private Integer equipmentId;

    private Integer productId;

    private Integer yield;

    private Integer unit;

    private Integer factoryId;

    private static final long serialVersionUID = 1L;
}