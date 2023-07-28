package com.hjp.testproject.controller.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KaKaoAPI {
    @Value("${api.kakao.rest}")
    private String KAKAO_REST_API_KEY;

    @Value("${api.kakao.redirect}")
    private String REDIRECT_URI;

    /**
     * code로 accessToken발급 후 유저정보 조회
     * 
     * @param accessToken
     * @return
     * @throws Exception
     */
    public ResponseEntity<String> getUserInfoToAccessToken(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers1.setBearerAuth(accessToken);

        HttpEntity<String> entity1 = new HttpEntity<>(headers1);
        return restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity1, String.class);
    }

}
