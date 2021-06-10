package com.fh.shop.goods.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Sku implements Serializable {



    private Long id;



    private String skuName;



    private Integer stock;



    private BigDecimal price;



    private  String specInfo;



    private Long spuId;



    private  Long  colorId;


    private String   image;




    private Integer count;



}
