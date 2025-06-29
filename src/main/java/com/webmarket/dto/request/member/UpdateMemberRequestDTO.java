package com.webmarket.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequestDTO {

    @Comment("이메일 인증")
    @NotNull(message = "이메일은 널이 들어올 수 없습니다")
    @NotBlank(message = "이메일은 공백이 들어올 수 없습니다")
    private String email;

    @Comment("유저 폰번호")
    @NotBlank(message = "핸드폰 번호는 공백이 들어올 수 없습니다")
    @NotNull(message = "핸드폰 번호는 널이 들어올 수 없습니다")
    private String phone;

    @Comment("닉네임")
    @NotBlank(message = "닉네임은 공백이 들어올 수 럾습니다")

    @Comment("프로필")
    private String profile;

    @Comment("주소")
    @NotNull(message = "주소는 널이 될 수 없습니다")
    private String address;

}
