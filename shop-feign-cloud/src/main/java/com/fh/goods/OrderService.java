package com.fh.goods;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.goods.po.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "shop-goods-api")
public interface OrderService {

    @GetMapping("/api/findSku")
    ServerResponse<Sku> findSku(@RequestParam("id") Long id);
}
