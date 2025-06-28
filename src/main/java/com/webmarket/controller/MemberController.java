package com.webmarket.controller;

import com.webmarket.dto.request.member.InsertMemberRequestDTO;
import com.webmarket.dto.request.member.InsertSocialMemberRequestDTO;
import com.webmarket.dto.request.member.ValidCheckEmailRequestDTO;
import com.webmarket.dto.request.member.ValidCheckNickRequestDTO;
import com.webmarket.dto.response.social.KakaoResponse;
import com.webmarket.dto.response.social.NaverResponse;
import com.webmarket.entitiy.Member;
import com.webmarket.service.member.MemberService;
import com.webmarket.service.member.MemberServiceImpl;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member/")
@Tag(name = "회원 컨트롤러",description = "회원에 관한 컨트롤러")
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final NaverService naverService;


    @Operation(summary = "이메일 유효검사" , description = "이메일 유효검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 존재x",content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "409",description = "이미 이메일이 존재",content =  @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500",description = "서버 오류",content =  @Content(schema = @Schema(implementation = Exception.class))),
    })
    @PostMapping("validCheckEmail")
    public ResponseEntity<?> validCheckEmail(@Valid @RequestBody ValidCheckEmailRequestDTO validCheckEmailRequestDTO) {
        log.info("===================== validCheckEmail START==================================");
        System.out.println("validCheckEmail  = " + validCheckEmailRequestDTO);
        return memberService.checkEmailDuplication(validCheckEmailRequestDTO.getEmail());
    }

    @Operation(summary = "닉네임 유효검사" , description = "닉네임 유효검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 존재x",content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "409",description = "이미 닉네임이 존재",content =  @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500",description = "서버 오류",content =  @Content(schema = @Schema(implementation = Exception.class))),
    })
    @PostMapping("validCheckEmail")
    public ResponseEntity<?> validCheckNick(@Valid @RequestBody ValidCheckNickRequestDTO validCheckNickRequestDTO) {
        log.info("===================== validCheckNick START==================================");
        System.out.println("validCheckNick  = " + validCheckNickRequestDTO);
        return memberService.checkEmailDuplication(validCheckNickRequestDTO.getNick());
    }


    @Operation(summary = "일반 회원가입" , description = "일반 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500",description = "서버 오류",content =  @Content(schema = @Schema(implementation = Exception.class))),
    })
    @PostMapping("insertMember")
    public ResponseEntity<?> insertMember(@Valid @RequestBody InsertMemberRequestDTO insertMemberDto, @RequestParam String type) {
        log.info("===================== insertMember START==================================");
        System.out.println("insertMemberDto = " + insertMemberDto);
        Member member = memberService.save(insertMemberDto);
        if(member != null && type.equals("app")){
            /*앱으로 회원가입시 */
          log.info("회원가입 성공");
            log.info("===================== insertMember Suc==================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 회원가입 실패");
        }
        if(member != null && type.equals("web")){
            /*웹 회원가입시 */
            log.info("회원가입 성공");
            log.info("===================== insertMember Suc==================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 회원가입 실패");
        }
        log.error("회원가입 실패");
        log.info("===================== insertMember Error==================================");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 회원가입 실패");
    }



    @Operation(summary = "소셜 회원가입" , description = "소셜 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500",description = "서버 오류",content =  @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "400" ,description = "소셜 인증 에러",content =  @Content(schema = @Schema(implementation =  Exception.class)))
    })
    @PostMapping("insertSocialMember")
    public ResponseEntity<?> insertSocialMember(@RequestBody InsertSocialMemberRequestDTO insertMemberDto,@RequestParam String type) {
        log.info("===================== insertSocialMember START==================================");
        System.out.println("insertMemberDto = " + insertMemberDto);
        Long changeEmail = Long.valueOf(insertMemberDto.getEmail());
        if(type.equals("kakao")) {
            KakaoResponse kakaoResponse = kakaoService.getKakaoUserCheck(insertMemberDto.getAccessToken(), changeEmail);
            if (kakaoResponse == null) {
                log.error("카카오 검증 실패");
                log.info("===================== insertSocialMember Error==================================");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 검증 실패");
            }
            Member check = memberService.socialSave(insertMemberDto);
            if (check != null) {
                log.error("서버 오류");
                log.info("===================== insertSocialMember Success==================================");
                return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
            }
            log.error("서버 오류");
            log.info("===================== insertSocialMember Error==================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 회원가입 실패");
        }else if( type.equals("naver")){
            NaverResponse naverResponse = naverService.getNaverUserCheck(insertMemberDto.getAccessToken(),changeEmail);
            if (naverResponse == null) {
                log.error("네이버 검증 실패");
                log.info("===================== insertSocialMember Error==================================");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("네이버 검증 실패");
            }
            Member check = memberService.socialSave(insertMemberDto);
            if (check != null) {
                log.info("회원가입 성공");

                log.info("===================== insertSocialMember Success==================================");
                return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
            }
            log.error("서버 오류");
            log.info("===================== insertSocialMember Error==================================");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 회원가입 실패");
        }
        log.error("잘못된 접근");
        log.info("===================== insertSocialMember Error==================================");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 회원가입 실패");
    }
}
