package com.webmarket.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidCheckNickRequestDTO {

    @NotNull(message = "닉네임은 널이 될 수 없습니다")
    @NotBlank(message = "닉네임에 공백 포함 x")
    private String nick;
}
