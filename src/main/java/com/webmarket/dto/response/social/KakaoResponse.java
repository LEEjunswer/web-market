package com.webmarket.dto.response.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KakaoResponse {
    /*카카오아이디 식별용*/
    private Long id;
    
}