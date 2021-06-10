package com.fh.shop.member.vo;

import lombok.Data;

import java.io.Serializable;

@Data

public class MemberVo implements Serializable {

    private Long id;


    private String memberName;//用户名


    private String nickName;// 昵称 绰号


    private String count; // 购物车个数
}
