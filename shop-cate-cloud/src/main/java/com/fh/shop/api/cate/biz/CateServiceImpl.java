package com.fh.shop.api.cate.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.cate.mapper.CateMapper;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "CateService")
public class CateServiceImpl implements CateService {

    @Resource
    private CateMapper cateMapper;

    @Override
    public ServerResponse toFinCate(){
        String cateList1 = RedisUtil.get("cateList");
        if (StringUtils.isNotEmpty(cateList1)){
            List<Cate> cateList = JSON.parseArray(cateList1, Cate.class);
            return ServerResponse.sucess(cateList);
        }
        List<Cate> cateList = cateMapper.selectList(null);
        RedisUtil.setex("cateList",JSON.toJSONString(cateList),30);
        return ServerResponse.sucess(cateList);
    }
}
