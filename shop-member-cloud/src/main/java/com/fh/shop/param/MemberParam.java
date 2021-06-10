package com.fh.shop.param;

import com.fh.shop.po.Member;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemberParam extends Member implements Serializable {

    private  String realPassword;

    private  String message;

}
