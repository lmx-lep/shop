package com.fh.shop.api.goods.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.goods.mapper.SkuMapper;
import com.fh.shop.goods.po.Sku;
import com.fh.shop.goods.vo.SkuVo;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.stream.Collectors;

@Service("skuService")
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;


    @Override
    public ServerResponse finSkuListByWhere(){
        String skuVoList1 = RedisUtil.get("skuVoList");
        if (StringUtils.isNotEmpty(skuVoList1)){
            return  ServerResponse.sucess(JSON.parseArray(skuVoList1,SkuVo.class));
        }
        List<Sku> skuList= skuMapper.finSkuListByWhere();
        List<SkuVo> skuVoList = skuList.stream().map(x ->{
            SkuVo skuVo = new SkuVo();
            skuVo.setId(x.getId());
            skuVo.setImage(x.getImage());
            skuVo.setPrice(x.getPrice());
            skuVo.setSkuName(x.getSkuName());
            return skuVo;
            }).collect(Collectors.toList());
        RedisUtil.setex("skuVoList",JSON.toJSONString(skuVoList),30);
        return ServerResponse.sucess(skuVoList);
    }

    @Override
    public ServerResponse findSku(Long id){
        Sku sku = skuMapper.selectById(id);
        return ServerResponse.sucess(sku);
    }


}
