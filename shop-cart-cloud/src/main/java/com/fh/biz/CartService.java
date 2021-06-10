package com.fh.biz;

import com.fh.shop.common.ServerResponse;

public interface CartService {
    ServerResponse addCart(Long memberId, Long count, Long skuId);

    ServerResponse deleteCart(Long memberId, Long skuId);

    ServerResponse deleteBatch(Long memberId, String ids);

}
