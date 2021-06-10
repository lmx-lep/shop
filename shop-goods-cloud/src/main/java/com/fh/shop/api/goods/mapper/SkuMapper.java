package com.fh.shop.api.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.goods.po.Sku;
import com.fh.shop.goods.vo.CartItemVo;

import java.util.List;

public interface SkuMapper extends BaseMapper<Sku> {

    List<Sku> finSkuListByWhere();

}
