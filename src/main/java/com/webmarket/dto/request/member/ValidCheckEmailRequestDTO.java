package com.webmarket.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValidCheckEmailRequestDTO {

    @NotNull(message = "이메일은 널이 될 수 없습니다")
    @NotBlank(message = "이메일에 공백 포함 x")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "유효한 이메일 주소를 입력해 주세요.")
    private String email;
}
