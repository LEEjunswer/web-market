package com.webmarket.service.member;

import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.entitiy.Member;
import com.webmarket.mapper.MemberMapper;
import com.webmarket.repository.member.MemberRepository;
import com.webmarket.util.passwordEncoder.Sha256PasswordEncoderWithSalt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final Sha256PasswordEncoderWithSalt passwordEncoder;

    @Override
    @Transactional
    public Member save(InsertMemberRequestDTO memberRequestDTO) {
        Member member = memberMapper.toEntityFromInsertDTO(memberRequestDTO);
        String encodedPassword = passwordEncoder.encode(memberRequestDTO.getPassword());
        member.setPassword(encodedPassword);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member socialSave(InsertSocialMemberRequestDTO insertSocialMemberRequestDTO) {
        return null;
    }

    @Override
    @Transactional
    public int updateRefreshToken(String email, String refreshToken, LocalDateTime expiration) {
        return memberRepository.updateRefreshTokenByEmail(email,refreshToken,expiration);
    }

    @Override
    @Transactional
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다. 이메일: " + email));
    }
}
