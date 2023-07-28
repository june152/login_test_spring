package com.hjp.testproject.mapper.table;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hjp.testproject.vo.MemberTestDataVO;

@Mapper
public interface DataTestMapper {
    public int insert(MemberTestDataVO vo) throws Exception;

    public int update(MemberTestDataVO vo) throws Exception;

    public int delete(MemberTestDataVO vo) throws Exception;

    public MemberTestDataVO getData(MemberTestDataVO vo);

    public MemberTestDataVO login(MemberTestDataVO vo) throws Exception;

    // Token Manage
    public int saveRefreshToken(Map<String, String> map) throws Exception;

    public String getRefreshToken(String userid) throws Exception;

    public int deleteRefreshToken(Map<String, String> map) throws Exception;

    public int idCheck(MemberTestDataVO vo) throws Exception;
}
