package com.webmarket.dto.response.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NaverResponse {
    /*네이버아이디 식별용*/
    private Long id;
    
}