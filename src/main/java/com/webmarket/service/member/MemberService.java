package com.webmarket.service.member;

import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.entitiy.Member;

import java.time.LocalDateTime;

public interface MemberService {

    /*이메일 회원가입*/
    Member save(InsertMemberRequestDTO memberRequestDTO);

    /*sns 회원가입*/
    Member socialSave(InsertSocialMemberRequestDTO insertSocialMemberRequestDTO);
    int updateRefreshToken(String email, String refreshToken, LocalDateTime expiration);

    Member getMemberByEmail(String email);
}
