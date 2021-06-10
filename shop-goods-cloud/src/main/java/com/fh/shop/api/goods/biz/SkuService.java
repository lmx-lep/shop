package com.fh.shop.api.goods.biz;


import com.fh.shop.common.ServerResponse;

public interface SkuService {
    ServerResponse finSkuListByWhere();


    ServerResponse findSku(Long id);
}
