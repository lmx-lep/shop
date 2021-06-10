package com.fh.shop.member.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Member implements Serializable {

    private Long id;
    private String memberName;//用户名

    private String password;

    private String nickName;// 昵称 绰号

    private String phone;

    private String mail;

    private int status;  //0 未激活 1 已激活

    private Long scorecard;

}
