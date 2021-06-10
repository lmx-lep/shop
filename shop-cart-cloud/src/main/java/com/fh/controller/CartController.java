package com.fh.controller;

import com.alibaba.fastjson.JSON;
import com.fh.biz.CartService;
import com.fh.shop.common.Attestation;
import com.fh.shop.common.BaseController;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SysCont;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.util.RedisUtil;
import com.fh.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CartController extends BaseController {


    @Resource(name = "cartService")
    private CartService cartService;


    @Autowired
    private HttpServletRequest request;

    @PostMapping("/cart")
    public ServerResponse addCart(Long count,Long skuId){
        MemberVo memberVo = getMemberVo(request);
        Long memberId = memberVo.getId();

        return cartService.addCart(memberId,count,skuId);
    }

    @GetMapping("/cart")
    public ServerResponse queryList(){
        MemberVo memberVo = getMemberVo(request);
        Long memberId = memberVo.getId();
        String info = RedisUtil.hget(Attestation.builderCartKey(memberId),SysCont.CARTJSON);
        CartVo cartVo = JSON.parseObject(info, CartVo.class);
        return ServerResponse.sucess(cartVo);
    }

    @DeleteMapping("/deleteCart")
    public ServerResponse deleteCart(Long skuId){
        MemberVo memberVo = getMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteCart(memberId,skuId);
    }
    @DeleteMapping("/deleteBatch")
    public ServerResponse deleteBatch(String ids){
        MemberVo memberVo = getMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteBatch(memberId,ids);
    }



}
