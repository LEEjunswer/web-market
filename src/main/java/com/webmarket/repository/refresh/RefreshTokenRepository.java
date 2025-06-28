package com.webmarket.repository.refresh;

import com.webmarket.entitiy.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
