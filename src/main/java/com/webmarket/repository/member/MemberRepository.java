package com.webmarket.repository.member;


import com.webmarket.entitiy.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.token = :token, rt.expiration = :expiration WHERE rt.member.email = :email")
    int updateRefreshTokenByEmail(@Param("email") String email, @Param("token") String token, @Param("expiration") LocalDateTime expiration);
}
