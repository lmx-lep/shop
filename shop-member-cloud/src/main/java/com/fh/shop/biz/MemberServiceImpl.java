package com.fh.shop.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.common.Attestation;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.common.SysCont;
import com.fh.shop.mapper.MemberMapper;
import com.fh.shop.member.vo.MemberVo;
import com.fh.shop.po.Member;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
@Service("memberService")
public class MemberServiceImpl implements MemberService {


    //    @Resource
    @Autowired
    private MemberMapper memberMapper;




    // 登录
    @Override
    public ServerResponse login(String memberName, String password) {
//          非空验证
        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(password)) {
            return ServerResponse.error(ResponseEnum.SPEC_IS_NULL);
        }
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName", memberName);
        Member member = memberMapper.selectOne(queryWrapper);
        if (StringUtils.isEmpty(member.toString())) {
            return ServerResponse.error(ResponseEnum.USER_IS_EMPTY);
        }//        先判断状态码是否激活
        if (member.getStatus() == SysCont.ZERO) {
            //如果未激活那就让他去激活啊  返回mail 和 id 给他方便激活不就行了吗
            Long id = member.getId();
            String mail = member.getMail();
            Map<String, String> map = new HashMap();
            map.put("id", id + "");
            map.put("mail", mail);
            String data = JSON.toJSONString(map);
            return ServerResponse.error(ResponseEnum.M_STATUS_IS_NOT, data);
        }
        // 用户名是否存在
        if (member == null) {
            return ServerResponse.error(ResponseEnum.USERNAME_IS_EMPTY);
        }
        //验证密码是否正确
        if (!Md5Util.md5(password).equals(member.getPassword())) {
            return ServerResponse.error(ResponseEnum.M_PASSWORD_ERR);
        }
        //生成签名 id+用户名+昵称   +.  + 秘钥
        MemberVo memberVo = new MemberVo();
        Long id = member.getId();
        memberVo.setId(id);
        memberVo.setMemberName(member.getMemberName());
        memberVo.setNickName(member.getNickName());
        //放入购物车个数
        String cartInfo = RedisUtil.hget(Attestation.builderCartKey(id), SysCont.CARTCOUNT);
        if (cartInfo != null) {
            if (!StringUtils.isEmpty(cartInfo)) {
                memberVo.setCount(cartInfo);
            }
        }
        String memberVoJSON = JSON.toJSONString(memberVo);
        // 签名 memberVoJSON+ SECRET
        String sign = Md5Util.sign(memberVoJSON, SysCont.SECRET);
//        统一障眼法 base64
        String signBase = Base64.getEncoder().encodeToString(sign.getBytes());
        String memberVoJSONBase = Base64.getEncoder().encodeToString(memberVoJSON.getBytes());
//         设置过期时长
        RedisUtil.setex(Attestation.builderNum(id), SysCont.NULL, SysCont.TOKEN);
        return ServerResponse.sucess(memberVoJSONBase + "." + signBase);
    }

}
