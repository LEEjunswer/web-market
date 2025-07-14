package com.webmarket.service.member;

import com.webmarket.dto.request.auth.LoginAuthRequestDTO;
import com.webmarket.dto.request.auth.SocialLoginAuthRequestDTO;
import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.dto.request.member.UpdateMemberRequestDTO;
import com.webmarket.entitiy.Member;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberService {

    /*이메일 회원가입*/
    Member save(InsertMemberRequestDTO memberRequestDTO);

    /*sns 회원가입*/
    Member socialSave(InsertSocialMemberRequestDTO insertSocialMemberRequestDTO,String type);

    int updateRefreshToken(String email, String refreshToken, LocalDateTime expiration);

    /*로그인시 FCM토큰 업데이트*/
    int updateFcmToken(String email, String fcmToken);

    Member getMemberByEmail(String email);
    /*로그인 소셜로그인*/
    Optional<Member> login(LoginAuthRequestDTO loginAuthRequestDTO);
    Optional<Member> socialLogin(SocialLoginAuthRequestDTO socialLoginAuthRequestDTO);


    ResponseEntity<?> checkEmailDuplication(String email);
    ResponseEntity<?> checkNickDuplication(String nick);

    int updateMember(UpdateMemberRequestDTO updateMemberRequestDTO);
    int updateLastLoginTime(Long id,LocalDateTime localDateTime);
}
