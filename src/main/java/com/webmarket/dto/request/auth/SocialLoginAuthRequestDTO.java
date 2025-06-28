package com.webmarket.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginAuthRequestDTO {

    @NotNull(message = "널이 될 수 없습니다")
    private String email;

    @NotNull(message = "sns 로그인 서버에서 엑세스 토큰 없이 진입 x")
    private String accessToken;

    @Comment("앱으로 로그인시 fcm 토큰")
    private String fcmToken;

    @Comment("마지막 접속일")
    private LocalDateTime lastLoginTime = LocalDateTime.now();
}
