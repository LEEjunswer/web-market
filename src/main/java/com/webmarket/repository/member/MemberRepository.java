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
    Optional<Member> findByNick(String nick);
    Optional<Member> findByEmailAndPassword(String email,String password);

    @Query("SELECT m FROM RefreshToken rt JOIN rt.member m WHERE rt.token = :token AND m.email = :email")
    Optional<Member> findByTokenAndEmail(@Param("token") String token, @Param("email") String email);


    @Modifying
    @Transactional
    @Query("UPDATE FcmToken ft SET ft.fcm = :fcmToken WHERE ft.member.email = :email")
    int updateFcmTokenByEmail(@Param("email") String email, @Param("fcmToken") String fcmToken);



    /*jpa는 member m 을 계속 줘야 한다 ibatis 랑 다름  */
    @Modifying
    @Transactional
    @Query("update Member m set m.lastLoginTime = :lastLoginTime where m.id = :id")
    int updateLastLoginTime(@Param("id") Long id, @Param("lastLoginTime") LocalDateTime lastLoginTime);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.token = :token, rt.expiration = :expiration WHERE rt.member.email = :email")
    int updateRefreshTokenByEmail(@Param("email") String email, @Param("token") String token, @Param("expiration") LocalDateTime expiration);
}
