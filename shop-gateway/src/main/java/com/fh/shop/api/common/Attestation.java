package com.fh.shop.api.common;

//验签类
public class Attestation {

    public static String builderNum(Long id){
        return "numberId:"+id;
    }


    public static String builderKey(String key){
        return "key:"+key;
    }

    public static String builderActiveKey(String key){
        return "ActiveId:"+key;
    }

    public static String builderCartKey(Long memberId){
        return "cartId:"+memberId;
    }


    public static String builderTokentKey(String tokenUUID){
        return "token:"+tokenUUID;
    }
}
