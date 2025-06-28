package com.webmarket.service.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webmarket.dto.response.social.NaverResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverService {

        private static final String NAVER_API_URL = "https://openapi.naver.com/v1/nid/me";

        public NaverResponse getNaverUserCheck(String accessToken, Long clientUserId) {

            System.out.println("accessToken = " + accessToken + " 클라이언트 Id = " + clientUserId);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        NAVER_API_URL,
                        HttpMethod.GET,
                        entity,
                        String.class
                );
                if (response.getStatusCode() == HttpStatus.OK) {
                    ObjectMapper mapper = new ObjectMapper();
                    NaverResponse naverResponse = mapper.readValue(response.getBody(), NaverResponse.class);
                    System.out.println("naverResponse.getId() = " + naverResponse.getId());


                    if (!naverResponse.getId().equals(clientUserId)) {
                        throw new RuntimeException("클라이언트 유저 아이디와 네이버에서 받은 아이디가 일치하지 않습니다.");
                    }
                    return naverResponse;
                } else {
                    throw new RuntimeException("네이버 유저 조회 실패: " + response.getStatusCode());
                }
            } catch (Exception e) {
                throw new RuntimeException("네이버 인증 실패", e);
            }
        }

}
