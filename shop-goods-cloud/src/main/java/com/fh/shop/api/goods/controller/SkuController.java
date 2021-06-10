package com.fh.shop.api.goods.controller;

import com.fh.shop.api.goods.biz.SkuService;
import com.fh.shop.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.GET;

@RestController
@RequestMapping("/api")
//@CrossOrigin
public class SkuController  {

    @Resource(name = "skuService")
    private SkuService skuService;


    @GetMapping("/skus")
    public ServerResponse finSkuListByWhere(){
        return skuService.finSkuListByWhere();
    }

    @GetMapping("/findSku")
    public ServerResponse findSku(@RequestParam("id") Long id){
        return skuService.findSku(id);
    }

}
