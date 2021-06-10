package com.fh.shop.common;

public final class SysCont {


//    秘钥
    public  static  final String SECRET="#@*)|>%09#@35@321^&^%$756%a#$xa@98#$%()";

//    过期时间
    public  static  final int TOKEN=30*60;

    public  static  final int TOKENS=5*60;

//    null
    public  static  final String NULL="";

//     放对象 取 删
    public  static  final String  MEMBER="member";

    //     放对象 取 删
    public  static  final String  CARTJSON="cartJson";

    public  static  final String  CARTCOUNT="count";

    public  static  final String  SELLER_ID="2088621955811480";

    public  static  final int  ERROR=-1;

    public  static  final int  SUCCESS=1;

    public  static  final int  ZERO=0;

    public  static  final int  ONE=1;

    public  static  final int  TWO=2;

    public  static  final int FORTY =40;

    public  static  final int TWENTY =20;

    public  static  final int ACTIVE=1;

    public interface Status{
        int zero=0;    //消息状态 0:投递中 1:投递成功 2:投递失败 3:消费成功
        int one=1;
        int two=2;
        int there=3;
        int four=3;

    }
}
