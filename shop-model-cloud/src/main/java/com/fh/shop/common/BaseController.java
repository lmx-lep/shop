package com.fh.shop.common;

import com.alibaba.fastjson.JSON;
import com.fh.shop.member.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BaseController {


    public static MemberVo getMemberVo(HttpServletRequest request) {
        try {
            String memberVoJson = URLDecoder.decode(request.getHeader(SysCont.MEMBER), "utf-8");
            return JSON.parseObject(memberVoJson, MemberVo.class);
        } catch (UnsupportedEncodingException e) {
           throw  new RuntimeException(e);
        }
    }


}
