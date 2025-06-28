package com.webmarket.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthRequestDTO {

    @NotNull(message = "널이 존재할 수 없습니다")
    @NotBlank(message = "이메일은 공백이 들어올 수 없습니다")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "유효한 이메일 주소를 입력해 주세요.")
    private String email;

    @Comment("이메일 가입시에만 비밀번호 필요")
    @NotNull(message = "비밀번호 null 존재 x")
    @NotBlank(message = "비밀번호 공백 불가")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;


    @Comment("앱으로 로그인시 fcm 토큰")
    private String fcmToken;
    
    @Comment("마지막 접속일")
    private LocalDateTime lastLoginTime = LocalDateTime.now();
}
