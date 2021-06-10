package com.fh.shop.biz;

import com.fh.shop.common.ServerResponse;

public interface MemberService {

    ServerResponse login(String memberName, String password);


}
