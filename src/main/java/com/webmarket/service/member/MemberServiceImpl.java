package com.webmarket.service.member;

import com.webmarket.dto.request.auth.LoginAuthRequestDTO;
import com.webmarket.dto.request.auth.SocialLoginAuthRequestDTO;
import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.dto.request.member.UpdateMemberRequestDTO;
import com.webmarket.entitiy.FcmToken;
import com.webmarket.entitiy.Member;
import com.webmarket.entitiy.MemberLoginType;
import com.webmarket.entitiy.RefreshToken;
import com.webmarket.mapper.MemberMapper;
import com.webmarket.repository.fcm.FcmTokenRepository;
import com.webmarket.repository.member.MemberCustomRepository;
import com.webmarket.repository.member.MemberRepository;
import com.webmarket.repository.refresh.RefreshTokenRepository;
import com.webmarket.util.passwordEncoder.Sha256PasswordEncoderWithSalt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final Sha256PasswordEncoderWithSalt passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberCustomRepository memberCustomRepository;

    @Override
    @Transactional
    public Member save(InsertMemberRequestDTO memberRequestDTO) {

        Member member = memberMapper.toEntityFromInsertDTO(memberRequestDTO);

        String encodedPassword = passwordEncoder.encode(memberRequestDTO.getPassword());
        member.setPassword(encodedPassword);
        Optional<Member> existingMember = memberRepository.findByEmail(member.getEmail());
        if (existingMember.isPresent()) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        member.setLoginType(MemberLoginType.LOCAL);

        Member savedMember = memberRepository.save(member);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMember(savedMember);


        FcmToken fcmToken = new FcmToken();
        fcmToken.setMember(savedMember);
        fcmTokenRepository.save(fcmToken);
        return savedMember;
    }
    @Override
    @Transactional
    public Member socialSave(InsertSocialMemberRequestDTO insertSocialMemberRequestDTO,String type) {


        Member member = memberMapper.toEntityFromInsertSocialDTO(insertSocialMemberRequestDTO);
        Optional<Member> existingMember = memberRepository.findByEmail(member.getEmail());
        if (existingMember.isPresent()) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        if(type.equals("kakao")){
            member.setLoginType(MemberLoginType.KAKAO);
        }else if(type.equals("naver")){
            member.setLoginType(MemberLoginType.NAVER);
        }
        Member savedMember = memberRepository.save(member);
    /*    RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMember(savedMember);
        refreshTokenRepository.save(refreshToken);
        FcmToken fcmToken = new FcmToken();
        fcmToken.setMember(savedMember);
        fcmTokenRepository.save(fcmToken);*/
        return savedMember;
    }

    @Override
    @Transactional
    public int updateRefreshToken(String email, String refreshToken, LocalDateTime expiration) {
        return memberRepository.updateRefreshTokenByEmail(email,refreshToken,expiration);
    }

    @Override
    @Transactional
    public int updateFcmToken(String email, String fcmToken) {
        return memberRepository.updateFcmTokenByEmail(email,fcmToken);
    }

    @Override
    @Transactional
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다. 이메일: " + email));
    }

    @Override
    @Transactional
    public Optional<Member> login(LoginAuthRequestDTO loginAuthRequestDTO) {
        String encodedPassword = passwordEncoder.encode(loginAuthRequestDTO.getPassword());
        return memberRepository.findByEmailAndPassword(loginAuthRequestDTO.getEmail(),encodedPassword);
    }

    @Override
    @Transactional
    public Optional<Member> socialLogin(SocialLoginAuthRequestDTO socialLoginAuthRequestDTO) {

        return memberRepository.findByEmail(socialLoginAuthRequestDTO.getEmail());
    }

    @Override
    @Transactional
    public ResponseEntity<?> checkEmailDuplication(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            // 중복된 이메일인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 사용 중인 이메일입니다.");
        }
        // 이메일 중복되지 않음
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<?> checkNickDuplication(String nick) {
        Optional<Member> member = memberRepository.findByNick(nick);
        if (member.isPresent()) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @Override
    public int updateMember(UpdateMemberRequestDTO updateMemberRequestDTO) {
        return memberCustomRepository.updateMember(updateMemberRequestDTO);
    }

    @Override
    public int updateLastLoginTime(Long id, LocalDateTime localDateTime) {
        return memberRepository.updateLastLoginTime(id,localDateTime);
    }
}
