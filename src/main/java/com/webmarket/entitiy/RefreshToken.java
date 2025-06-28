package com.webmarket.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;


    @Comment("JWT 리프레쉬 토큰")
    @Column(nullable = false,unique = true)
    private String token;


    @Comment("리프레시토큰 만료 날짜")
    @Column(nullable = false)
    private LocalDateTime expiration;

}
