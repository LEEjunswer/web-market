package com.webmarket.controller;

import com.webmarket.dto.request.auth.LoginAuthRequestDTO;
import com.webmarket.dto.request.auth.SocialLoginAuthRequestDTO;
import com.webmarket.dto.request.member.ValidCheckNickRequestDTO;
import com.webmarket.dto.response.social.KakaoResponse;
import com.webmarket.dto.response.social.NaverResponse;
import com.webmarket.entitiy.Member;
import com.webmarket.service.member.MemberService;
import com.webmarket.service.social.KakaoService;
import com.webmarket.service.social.NaverService;
import com.webmarket.util.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/")
@Tag(name = "인증 컨트롤러" ,description = "인증에 관한 컨트롤러")
public class AuthController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "일반 이메일 로그인", description = "일반 이메일 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = JwtUtil.class))),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 실패", content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginAuthRequestDTO loginAuthRequestDTO, @RequestParam String type) {
        log.info("===================== login START==================================");
        log.info("loginAuthRequestDTO  = {}", loginAuthRequestDTO);
        Optional<Member> member = memberService.login(loginAuthRequestDTO);
        if (member.isPresent()) {
            String email = member.get().getEmail();
            String role = member.get().getRole().name();
            String refreshToken = jwtUtil.generateRefreshToken(email);
            String accessToken = jwtUtil.generateAccessToken(email, role);
            LocalDateTime localDateTime = LocalDateTime.now();
            memberService.updateLastLoginTime(member.get().getId(), localDateTime);
            if (type.equals("app")) {
                log.info("앱 로그인 성공");
                LocalDateTime expiration = LocalDateTime.now().plusDays(7);
                memberService.updateFcmToken(loginAuthRequestDTO.getEmail(),loginAuthRequestDTO.getFcmToken());
                memberService.updateRefreshToken(member.get().getEmail(), refreshToken, expiration);
                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Refresh-Token", refreshToken)
                        .body("로그인 성공");
            } else if (type.equals("web")) {
                log.info("웹 로그인 성공");
                ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", "Bearer " + accessToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(60 * 60 * 24 * 7)
                        .sameSite("Strict")
                        .build();

                ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(60 * 60 * 24 * 7)
                        .sameSite("Strict")
                        .build();

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body("로그인 성공");
            }
        }

        log.warn("로그인 실패: 잘못된 이메일 또는 비밀번호");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 이메일 또는 비밀번호");
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = JwtUtil.class))),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 실패", content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping("socialLogin")
    public ResponseEntity<?> socialLogin(@Valid @RequestBody SocialLoginAuthRequestDTO socialLoginAuthRequestDTO, @RequestParam String type,@RequestParam String sns) {
        log.info("===================== socialLogin START==================================");
        log.info("loginAuthRequestDTO  = {}", socialLoginAuthRequestDTO);
        Optional<Member> member = memberService.socialLogin(socialLoginAuthRequestDTO);

        if (member.isPresent()) {
            String email = member.get().getEmail();
            String role = member.get().getRole().name();
            String refreshToken = jwtUtil.generateRefreshToken(email);
            String accessToken = jwtUtil.generateAccessToken(email, role);
            LocalDateTime localDateTime = LocalDateTime.now();
            memberService.updateLastLoginTime(member.get().getId(), localDateTime);
            Long changeEmail = Long.valueOf(socialLoginAuthRequestDTO.getEmail());
            if (type.equals("app")) {
                if(sns.equals("kakao")){
                    KakaoResponse kakaoResponse = kakaoService.getKakaoUserCheck(socialLoginAuthRequestDTO.getEmail(),changeEmail);
                    if (kakaoResponse == null) {
                        log.error("카카오 검증 실패");
                        log.warn("===================== socialLogin Error==================================");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 검증 실패");
                    }
                    log.info("앱 로그인 성공");
                    LocalDateTime expiration = LocalDateTime.now().plusDays(7);
                    memberService.updateFcmToken(socialLoginAuthRequestDTO.getEmail(),socialLoginAuthRequestDTO.getFcmToken());
                    memberService.updateRefreshToken(member.get().getEmail(), refreshToken, expiration);
                    return ResponseEntity.ok()
                            .header("Authorization", "Bearer " + accessToken)
                            .header("Refresh-Token", refreshToken)
                            .body("로그인 성공");
                }else if(sns.equals("naver")){
                    NaverResponse naverResponse = naverService.getNaverUserCheck(socialLoginAuthRequestDTO.getEmail(),changeEmail);
                    if (naverResponse == null) {
                        log.error("네이버 검증 실패");
                        log.info("===================== socialLogin Error==================================");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("네이버 검증 실패");
                    }
                    log.info("앱 로그인 성공");
                    LocalDateTime expiration = LocalDateTime.now().plusDays(7);
                    memberService.updateFcmToken(socialLoginAuthRequestDTO.getEmail(),socialLoginAuthRequestDTO.getFcmToken());
                    memberService.updateRefreshToken(member.get().getEmail(), refreshToken, expiration);
                    return ResponseEntity.ok()
                            .header("Authorization", "Bearer " + accessToken)
                            .header("Refresh-Token", refreshToken)
                            .body("로그인 성공");
                }
            } else if (type.equals("web")) {
                if(sns.equals("kakao")){
                    KakaoResponse kakaoResponse = kakaoService.getKakaoUserCheck(socialLoginAuthRequestDTO.getEmail(),changeEmail);
                    if (kakaoResponse == null) {
                        log.error("카카오 검증 실패");
                        log.warn("===================== socialLogin Error==================================");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 검증 실패");
                    }
                    log.info("웹 로그인 성공");
                    ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", "Bearer " + accessToken)
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(60 * 60 * 24 * 7)
                            .sameSite("Strict")
                            .build();

                    ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(60 * 60 * 24 * 7)
                            .sameSite("Strict")
                            .build();

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                            .body("로그인 성공");
                }}else if(sns.equals("naver")){
                NaverResponse naverResponse = naverService.getNaverUserCheck(socialLoginAuthRequestDTO.getEmail(),changeEmail);
                if (naverResponse == null) {
                    log.error("네이버 검증 실패");
                    log.info("===================== socialLogin Error==================================");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("네이버 검증 실패");
                }
                log.info("웹 로그인 성공");
                ResponseCookie accessTokenCookie = ResponseCookie.from("Authorization", "Bearer " + accessToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(60 * 60 * 24 * 7)
                        .sameSite("Strict")
                        .build();

                ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(60 * 60 * 24 * 7)
                        .sameSite("Strict")
                        .build();

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body("로그인 성공");
            }
        }

        log.warn("로그인 실패: 엑세스 토큰 or id 실패");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 엑세스토큰 or id");
    }

    @Operation(summary = "엑세스 토큰 만료시 재발급", description = "리프레시 토큰으로 엑세스 토큰만 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "RefreshToken 만료됨", content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("=====================  AccessToken 재발급 START =========================");
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 만료됨. 다시 로그인하세요.");
        }

        String email = jwtUtil.getMemberIdFromToken(refreshToken);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 RefreshToken입니다.");
        }

        Member member = memberService.getMemberByEmail(email);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("해당 이메일의 회원이 존재하지 않습니다.");
        }

        String role = member.getRole().name();
        String newAccessToken = JwtUtil.generateAccessToken(email, role);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .body("AccessToken 재발급 완료");
    }
}



