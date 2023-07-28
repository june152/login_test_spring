package com.hjp.testproject.controller.utils;

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
    private final String KAKAO_REST_API_KEY = "c11b41a51e8b01d4961287d02726bf56";
    private final String REDIRECT_URI = "http://localhost:3000/redirect";

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
