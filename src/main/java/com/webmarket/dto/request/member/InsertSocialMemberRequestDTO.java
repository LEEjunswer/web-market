package com.webmarket.dto.request.member;


import com.webmarket.entitiy.MemberGrade;
import com.webmarket.entitiy.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertSocialMemberRequestDTO {

    @Comment("회원 이메일")
    @NotNull(message = "null이 들어올 수 없습나다")
    @NotBlank(message = "공백 불가")
    private String email;

    @Comment("닉네임")
    @NotNull(message = "닉네임은 null 존재 x")
    @NotBlank(message = "닉네임 공백 불가")
    private String nick;

    @Comment("생년월일")
    @NotNull(message = "생년월일은 필수입니다.")
    @NotBlank(message = "생년월일은 공백이 들어올 수 없습니다")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식은 yyyy-MM-dd여야 합니다.")
    private String birth;


    @Comment("각 소셜 토큰")
    @NotNull(message = "토큰은 널이 될 수 없습니다.")
    private String accessToken;

    @Comment("유저 이름")
    @NotNull(message = "이름은 null 존재 x")
    @NotBlank(message = "이름은 공백 불가")
    private String name;

    @Comment("핸드폰 번호")
    @NotNull(message = "핸드폰 번호는 필수 입니다")
    @NotBlank(message = "핸드폰 번호는 공백이 들어올 수 없습니다")
    @Pattern(regexp = "^01[016789]-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식은 010, 011, 016, 017, 018, 019-XXXX-XXXX 형식이어야 합니다.")
    private String phone;

    @Comment("유저 프로필")
    private String profile;


    @Comment("주소")
    @NotNull(message = "주소는 널이 들어올 수 없습니다")
    private String address;


    /*회원가입시 디폴트 유저로 가입*/
    private MemberRole role = MemberRole.USER;
    /*회원가입시 디폴트 브론즈로 가입*/
    private MemberGrade grade = MemberGrade.BRONZE;
}
