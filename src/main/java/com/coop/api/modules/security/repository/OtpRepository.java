package com.coop.api.modules.security.repository;

import com.coop.api.core.entity.User;
import com.coop.api.modules.security.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    public List<Otp> findByUserOrderByCreatedAtDesc(User user);
}
