package com.fh.shop.goods.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartItemVo implements Serializable {

    private Long skuId;

    private Long count;

    private String skuName;

    private String skuImage;

    private String subPrice;

    private String price;


}
