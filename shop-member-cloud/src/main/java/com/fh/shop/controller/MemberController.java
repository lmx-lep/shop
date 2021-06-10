package com.fh.shop.controller;

import com.alibaba.fastjson.JSON;
import com.fh.shop.biz.MemberService;
import com.fh.shop.common.Attestation;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SysCont;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Resource(name = "memberService")
    private MemberService memberService;

    @Autowired
    private HttpServletRequest request;


    //    登录
    @PostMapping("/mem/login")
    public ServerResponse login(String memberName, String password) {
        return memberService.login(memberName, password);
    }

    //    导航条
    @GetMapping("/mem/loginFind")
    public ServerResponse loginFind() throws UnsupportedEncodingException {
        String memberVoJson = URLDecoder.decode(request.getHeader(SysCont.MEMBER),"utf-8");
        MemberVo memberVo = JSON.parseObject(memberVoJson, MemberVo.class);
        return ServerResponse.sucess(memberVo);
    }


    //    删除request 注销
    @GetMapping("/mem/deleteLog")
    public ServerResponse deleteLog() throws UnsupportedEncodingException {
            String memberVoJson = URLDecoder.decode(request.getHeader(SysCont.MEMBER),"utf-8");
        MemberVo memberVo = JSON.parseObject(memberVoJson, MemberVo.class);
        RedisUtil.delete(Attestation.builderNum(memberVo.getId()));
        return ServerResponse.sucess();
    }


}
