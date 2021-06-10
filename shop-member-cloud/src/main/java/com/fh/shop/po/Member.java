package com.fh.shop.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class Member implements Serializable {

    private Long id;
    @ApiModelProperty("用户名")
    private String memberName;//用户名

    private String password;

    private String nickName;// 昵称 绰号

    private String phone;

    private String mail;

    private int status;  //0 未激活 1 已激活

    private Long scorecard;

}
