package com.webmarket.service.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webmarket.dto.response.social.KakaoResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class KakaoService {


    public KakaoResponse getKakaoUserCheck(String accessToken, Long clientUserId) {

        System.out.println("accessToken + = " + accessToken + "클라이언트 Id" +clientUserId);
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                KakaoResponse kakaoResponse = mapper.readValue(response.getBody(), KakaoResponse.class);
                System.out.println("kakaoResponse.getId() = " + kakaoResponse.getId());
                if (!kakaoResponse.getId().equals(clientUserId)) {
                    throw new RuntimeException("클라이언트 유저 아이디와 카카오에서 받은 아이디하고 일치 X");
                }
                return kakaoResponse;
            } else {
                throw new RuntimeException("카카오 유저 조회 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("카카오 인증 실패", e);
        }
    }
}
