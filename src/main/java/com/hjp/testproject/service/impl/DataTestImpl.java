package com.hjp.testproject.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hjp.testproject.mapper.table.DataTestMapper;
import com.hjp.testproject.service.IDefaultService;
import com.hjp.testproject.vo.MemberTestDataVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataTestImpl implements IDefaultService<MemberTestDataVO, MemberTestDataVO> {
    private final DataTestMapper mapper;

    @Override
    public int insert(MemberTestDataVO vo) throws Exception {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (vo.getUserPwd() != null) {
            vo.setUserPwd(passwordEncoder.encode(vo.getUserPwd()));
        }
        // 이메일 암호화
        if (vo.getEmail() != null) {
            vo.encode();
        }
        return mapper.insert(vo);
    }

    @Override
    public int update(MemberTestDataVO vo) throws Exception {
        return mapper.update(vo);
    }

    @Override
    public int delete(MemberTestDataVO vo) throws Exception {
        return mapper.delete(vo);
    }

    public MemberTestDataVO getData(MemberTestDataVO vo) {
        return mapper.getData(vo);
    }

    public MemberTestDataVO login(MemberTestDataVO vo) throws Exception {
        if (vo.getUserId() == null || vo.getUserPwd() == null) {
            return null;
        }
        return mapper.login(vo);
    }

    public int saveRefreshToken(String userid, String refreshToken) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userid);
        map.put("refreshToken", refreshToken);
        return mapper.saveRefreshToken(map);
    }

    public String getRefreshToken(String userid) throws Exception {
        // TODO Auto-generated method stub
        return mapper.getRefreshToken(userid);
    }

    public int deleteRefreshToken(String userid) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userid);
        map.put("refreshToken", null);
        return mapper.deleteRefreshToken(map);
    }

    public boolean idCheck(MemberTestDataVO vo) throws Exception {
        // TODO Auto-generated method stub
        return mapper.idCheck(vo) == 0;
    }
}
