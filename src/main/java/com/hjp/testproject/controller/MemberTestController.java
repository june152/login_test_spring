package com.hjp.testproject.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjp.testproject.controller.utils.KaKaoAPI;
import com.hjp.testproject.service.impl.DataTestImpl;
import com.hjp.testproject.service.impl.jwtServiceImpl;
import com.hjp.testproject.vo.MemberTestDataVO;
import com.hjp.testproject.vo.form.KaKaoResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/member")
public class MemberTestController {

    public static final Logger logger = LoggerFactory.getLogger(MemberTestController.class);
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    private final DataTestImpl dataTestImpl;
    private final jwtServiceImpl jwtImpl;
    private final KaKaoAPI kaKaoAPI;

    @PostMapping("/getData")
    public MemberTestDataVO getData(MemberTestDataVO vo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String token = "";
        String id = "";
        Cookie[] list = request.getCookies();
        // 쿠키 없음
        if (list == null) {
            return null;
        }
        for (Cookie cookie : list) {
            if (cookie.getName().equals("refresh_token")) {
                token = cookie.getValue();
            } else if (cookie.getName().equals("id")) {
                id = cookie.getValue();
            }
        }
        // id 값 없음
        if (id == null) {
            return null;
        }

        if (jwtImpl.checkToken(token)) {
            System.out.println("토큰 유효");
            String refreshToken = jwtImpl.createRefreshToken("userid", id);
            dataTestImpl.saveRefreshToken(id, refreshToken);
            Cookie cookie = new Cookie("refresh_token", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            Cookie cookie2 = new Cookie("refresh_token", refreshToken);
            cookie2.setHttpOnly(true);
            cookie2.setSecure(true);
            cookie2.setPath("/");
            response.addCookie(cookie2);
            vo.setUserId(id);
            return dataTestImpl.getData(vo);
        } else {
            System.out.println("토큰 만료");
            Cookie cookie = new Cookie("refresh_token", null);
            Cookie idCookie = new Cookie("id", null);
            cookie.setMaxAge(0);
            idCookie.setMaxAge(0);
            cookie.setPath("/");
            idCookie.setPath("/");
            response.addCookie(cookie);
            response.addCookie(idCookie);
            return null;
        }
    }

    // ID값 체크(중복확인)
    @GetMapping("/idCheck")
    public ResponseEntity<String> idCheck(KaKaoResponse res) {
        Map<String, String> resultMap = new HashMap<>();
        MemberTestDataVO memberTestDataVO = new MemberTestDataVO();
        memberTestDataVO.setUserId(res.getId());
        memberTestDataVO.setSocialType(res.getSocialType());
        System.out.println(res.getId());
        resultMap.put("userId", res.getId());
        System.out.println(res.getSocialType());
        resultMap.put("socialType", res.getSocialType());
        try {
            if (dataTestImpl.idCheck(memberTestDataVO)) { // ID 없음
                return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
            } else { // ID 있음
                return new ResponseEntity<String>(FAIL, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(MemberTestDataVO param, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        logger.info("로그인 요청");
        MemberTestDataVO vo = new MemberTestDataVO();

        try {
            // 일반 회원
            if (param.getSocialType() == null) {
                vo = dataTestImpl.login(param);
            } else {
                vo = param;
            }

            if (vo != null) {
                String accessToken = jwtImpl.createAccessToken("userid", vo.getUserId());// key, data
                String refreshToken = jwtImpl.createRefreshToken("userid", vo.getUserId());
                System.out.println(refreshToken);
                if (dataTestImpl.idCheck(vo)) { // 가입된 ID 없음
                    // 가입처리 로직 -> 회원가입을 통해 추가적 정보 입력 필요한 경우 사전에 프로세스 변경 필요
                    // dataTestImpl.insert(vo);
                    resultMap.put("message", FAIL);
                    status = HttpStatus.ACCEPTED;
                    return new ResponseEntity<Map<String, Object>>(resultMap, status);
                }
                dataTestImpl.saveRefreshToken(vo.getUserId(), refreshToken);
                logger.debug("access토큰정보 : {}", accessToken);
                logger.debug("refresh 토큰정보 : {}", refreshToken);
                resultMap.put("access_token", accessToken);
                Cookie cookie = new Cookie("refresh_token", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                Cookie id = new Cookie("id", vo.getUserId());
                response.addCookie(cookie);
                response.addCookie(id);
                resultMap.put("message", SUCCESS);
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", FAIL);
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @PostMapping("/refresh")
    public Map<String, Object> refreshToken(MemberTestDataVO user, HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Boolean status = true;
        String token = "";
        String id = "";
        Cookie[] list = request.getCookies();
        // 쿠키 없음
        if (list == null) {
            status = false;
            resultMap.put("success", status);
            return resultMap;
        }
        for (Cookie cookie : list) {
            if (cookie.getName().equals("refresh_token")) {
                token = cookie.getValue();
            } else if (cookie.getName().equals("id")) {
                id = cookie.getValue();
            }
        }
        // id 값 없음
        if (id == null) {
            status = false;
            resultMap.put("success", status);
            return resultMap;
        }
        if (jwtImpl.checkToken(token)) {
            System.out.println("토큰 유효");
            resultMap.put("success", status);
            String refreshToken = jwtImpl.createRefreshToken("userid", user.getUserId());
            dataTestImpl.saveRefreshToken(user.getUserId(), refreshToken);
            Cookie cookie = new Cookie("refresh_token", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            Cookie cookie2 = new Cookie("refresh_token", refreshToken);
            cookie2.setHttpOnly(true);
            cookie2.setSecure(true);
            cookie2.setPath("/");
            response.addCookie(cookie2);
        } else {
            System.out.println("토큰 만료");
            status = false;
            Cookie cookie = new Cookie("refresh_token", null);
            Cookie idCookie = new Cookie("id", null);
            cookie.setMaxAge(0);
            idCookie.setMaxAge(0);
            cookie.setPath("/");
            idCookie.setPath("/");
            response.addCookie(cookie);
            response.addCookie(idCookie);

            resultMap.put("success", status);
        }

        return resultMap;
    }

    @GetMapping("/getUserInfoToAccessToken")
    public Map<String, Object> getUserInfoToAccessToken(String accessToken) {

        try {
            ResponseEntity<String> tokenInfo = kaKaoAPI.getUserInfoToAccessToken(accessToken);
            System.out.println(tokenInfo.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = (Map<String, Object>) objectMapper.readValue(tokenInfo.getBody(),
                    Map.class);
            System.out.println("responseMap : " + responseMap);
            return responseMap;
        } catch (Exception e) {
            logger.error("정보 불러오기 실패 : {}", e);
            return null;
        }

    }
}
