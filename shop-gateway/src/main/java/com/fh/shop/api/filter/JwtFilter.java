package com.fh.shop.api.filter;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.common.Attestation;
import com.fh.shop.api.common.MemberVo;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SysCont;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;


@Component
public class JwtFilter extends ZuulFilter {


    @Value("${com.fh.url}")
    private List<String> urlList;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }


    @Override
    public int filterOrder() {
        return 1;
    }


    @Override
    public boolean shouldFilter() {
        return true;
    }


    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        boolean isCheck = false;
        for (String url : urlList) {
            if (requestURL.indexOf(url) > 0) {
                isCheck = true;
                break;
            }
        }
        if (!isCheck) {
//            默认放行
            return null;
        }
//        否则
        //验证验签 通过转换让返回的错误信息进行返回并提升
        String header = request.getHeader("x-auth");
//        判断是否有头信息
        if (StringUtils.isEmpty(header)){
            return buildResponse(ResponseEnum.TOKEN_NULL);
        }
//        判断请求头信息是否符合要求
        String[] headerArr = header.split("\\.");
        if (headerArr.length!=2){
            return buildResponse(ResponseEnum.TOKEN_NOT);
        }
//        剖析判断
        String memberVoJSONBase = headerArr[0];
        String signBase = headerArr[1];
        String memberVoJSON = new String(Base64.getDecoder().decode(memberVoJSONBase),"utf-8");
        String sign= new String(Base64.getDecoder().decode(signBase), "utf-8");
        String newSign = Md5Util.sign(memberVoJSON, SysCont.SECRET);
//        不符合信息 黑客入侵或者数据出现异常
        if (!sign.equals(newSign)){
            return buildResponse(ResponseEnum.TOKEN_ERR);
        }
//        续命
        MemberVo memberVo = JSON.parseObject(memberVoJSON, MemberVo.class);
        Long id = memberVo.getId();
//        查询购物车
        String cartInfo = RedisUtil.hget(Attestation.builderCartKey(id),SysCont.CARTCOUNT);
        if (cartInfo!=null){
            if (!StringUtils.isEmpty(cartInfo)){
                memberVo.setCount(cartInfo);
            }
        }
//        判断还是否存在 不存在就是没有登录成功
        if (!RedisUtil.exist(Attestation.builderNum(id))){
            return buildResponse(ResponseEnum.TOKEN_NOT_EXIST);

        }
//        重新续命
        RedisUtil.expire(Attestation.builderNum(id),SysCont.TOKEN);
//        把对象放到当前请求中
        String memberVoJson = JSON.toJSONString(memberVo);

        currentContext.addZuulRequestHeader(SysCont.MEMBER,URLEncoder.encode(memberVoJson,"utf-8"));
//        request.setAttribute(SysCont.MEMBER,memberVo);
        return null;
    }


    public Object buildResponse(ResponseEnum responseEnum){
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json:charset=utf-8");
        currentContext.setSendZuulResponse(false);//拦截当前请求 不在进行路由转发
        ServerResponse error = ServerResponse.error(responseEnum);
        String res = JSON.toJSONString(error);
        currentContext.setResponseBody(res);
        return null;
    }

}
