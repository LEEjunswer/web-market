package com.webmarket.entitiy;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @Comment("회원 고유 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id" ,nullable = false)
    private Long id;

    @Comment("회원 이메일")
    @NotNull(message = "null이 들어올 수 없습나다")
    @NotBlank(message = "공백 불가")
    @Column(nullable = false,unique = true,updatable = false)
    private String email;


    @Comment("닉네임")
    @NotNull(message = "닉네임은 null 존재 x")
    @NotBlank(message = "닉네임 공백 불가")
    @Column(nullable = false,unique = true)
    private String nick;


    @Comment("이메일 가입시에만 비밀번호 필요")
    private String password;

    @Comment("유저 이름")
    @NotNull(message = "이름은 null 존재 x")
    @NotBlank(message = "이름은 공백 불가")
    @Column(nullable = false,updatable = false)
    private String name;

    @Comment("생년월일")
    @NotNull(message = "생년월일은 필수입니다.")
    @NotBlank(message = "생년월일은 공백이 들어올 수 없습니다")
    @Column(nullable = false,updatable = false)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식은 yyyy-MM-dd여야 합니다.")
    private String birth;

    @Comment("핸드폰 번호")
    @NotNull(message = "핸드폰 번호는 필수 입니다")
    @NotBlank(message = "핸드폰 번호는 공백이 들어올 수 없습니다")
    @Column(nullable = false)
    @Pattern(regexp = "^01[016789]-\\d{4}-\\d{4}$", message = "핸드폰 번호 형식은 010, 011, 016, 017, 018, 019-XXXX-XXXX 형식이어야 합니다.")
    private String phone;


    @Enumerated(EnumType.STRING)
    @Comment("회원 권한")
    @Column(nullable = false)
    private MemberRole role;
    
    @Enumerated(EnumType.STRING)
    @Comment("회원 등급")
    @Column(nullable = false)
    private MemberGrade grade;

    @Comment("주소 동까지만 표현할 예정 앱같은 경우 내위치 기반으로 계속 업데이트")
    @Column(nullable = false)
    @NotNull(message = "주소는 널이 들어올 수 없습니다")
    private String address;

    @Comment("유저 프로필")
    private String profile;

    @Comment("회원탈퇴 유무 false 면 미탈퇴")
    private boolean isDeleted =false;

    @Comment("회원가입 일자")
    @NotNull(message = "회원가입일자는 널이 될 수 없습니다")
    @Column(nullable = false,updatable = false)
    private LocalDateTime createTime;

    @Comment("마지막 접속일")
    private LocalDateTime lastLoginTime = LocalDateTime.now();

    @Comment("회원탈퇴일자")
    private String deleteTime;

    @Comment("회원가입 타입형")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "login_type")
    @NotNull(message = "회원가입 타입형이 널이 될 수 없습니다.")
    private MemberLoginType loginType;

    /*회원가입 자동으로 됨*/
    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
    }
}
