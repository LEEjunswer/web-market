package com.webmarket.auth;

import com.webmarket.entitiy.Member;
import com.webmarket.service.member.MemberService;
import com.webmarket.util.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String accessToken = resolveToken(request, "Authorization");
        String refreshToken = resolveToken(request, "Refresh-Token");

        if (accessToken != null) {
            try {

                String email = jwtUtil.getMemberIdFromToken(accessToken);
                Member member = memberService.getMemberByEmail(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(member, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
                return;

            } catch (Exception e) {
                log.error("액세스 토큰 검증 실패: {}", e.getMessage());
            }
        }
        if (refreshToken != null) {
            try {
                String email = jwtUtil.getMemberIdFromToken(refreshToken);
                log.info("🔑 파싱된 이메일: {}", email);

                Member member = memberService.getMemberByEmail(email);
                if (member == null) {
                    log.error("❌ 이메일은 있으나 해당 유저가 DB에 없음: {}", email);
                    throw new RuntimeException("회원 없음");
                }

                String newAccessToken = JwtUtil.generateAccessToken(email);
                String newRefreshToken = JwtUtil.generateRefreshToken(email);

                memberService.updateRefreshToken(email, newRefreshToken);

                response.setHeader("Authorization", "Bearer " + newAccessToken);
                response.setHeader("Refresh-Token", "Bearer " + newRefreshToken);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(member, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
                return;

            } catch (Exception e) {
                log.error("리프레시 토큰 검증 실패: {}", e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "리프레시 토큰 만료. 다시 로그인하세요.");
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/games/ws/");
    }
}
