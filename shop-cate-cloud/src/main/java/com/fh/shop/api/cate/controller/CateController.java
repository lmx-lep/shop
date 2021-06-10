package com.fh.shop.api.cate.controller;

import com.fh.shop.api.cate.biz.CateService;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@Api(tags = "分类接口文档")
//@CrossOrigin
public class CateController{

    @Resource(name = "CateService")
    private CateService cateservice;

    @GetMapping("cates")
    @ApiOperation("查询分类列表")
    public ServerResponse toFinCate(){
        return cateservice.toFinCate();
    }


}
