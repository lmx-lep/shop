package com.fh.vo;

import com.fh.shop.goods.vo.CartItemVo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartVo implements Serializable {

    private List<CartItemVo> cartItemVoList=new ArrayList<CartItemVo>();

    private String totalPrice;

    private Long totalCount;
}
