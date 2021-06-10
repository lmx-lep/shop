package com.fh.shop.goods.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Spu implements Serializable {

    private  Long id;

    private String spuName;

    private BigDecimal price;

    private Integer stock;

    private Long cate1;

    private Long cate2;

    private Long cate3;

    private Long brandId;

    private String attrInfo;

    private String specInfo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GTM+8")
    private Date createDate;

    private  String brandName;

    private String cateName;

    private Integer  statues;//1:上架/2:下架

    private Integer  degree;//1:新品/2:非新品

    private Integer  recommend;//1:推荐/2:不推荐

}
