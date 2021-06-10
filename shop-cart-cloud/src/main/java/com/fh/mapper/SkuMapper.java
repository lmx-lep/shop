package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.goods.po.Sku;
import com.fh.shop.goods.vo.CartItemVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SkuMapper extends BaseMapper<Sku> {

    List<Sku> finSkuListByWhere();

    int updateStatusById(CartItemVo cartItemVo);
}
