package com.fh.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.member.po.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

}
