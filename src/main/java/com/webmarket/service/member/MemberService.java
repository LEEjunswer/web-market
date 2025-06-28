package com.webmarket.service.member;

import com.webmarket.dto.request.auth.LoginAuthRequestDTO;
import com.webmarket.dto.request.auth.SocialLoginAuthRequestDTO;
import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.entitiy.Member;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberService {

    /*이메일 회원가입*/
    Member save(InsertMemberRequestDTO memberRequestDTO);

    /*sns 회원가입*/
    Member socialSave(InsertSocialMemberRequestDTO insertSocialMemberRequestDTO);
    int updateRefreshToken(String email, String refreshToken, LocalDateTime expiration);
    int updateFcmToken(String email, String fcmToken);
    Member getMemberByEmail(String email);

    Optional<Member> login(LoginAuthRequestDTO loginAuthRequestDTO);
    Optional<Member> socialLogin(SocialLoginAuthRequestDTO socialLoginAuthRequestDTO);


    ResponseEntity<?> checkEmailDuplication(String email);
    ResponseEntity<?> checkNickDuplication(String nick);
}
