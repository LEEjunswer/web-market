package com.webmarket.repository.fcm;

import com.webmarket.entitiy.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FcmTokenRepository extends JpaRepository<FcmToken,Long> {
}
