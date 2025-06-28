package com.webmarket.util.jwt;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtToken {
    /*Jwt 토큰 넘버*/
    private String refreshToken;
    /*유저 이메일*/
    private String email;
    
    /*Jwt 토큰 엑세스토큰*/
    private String accessToken;
}
