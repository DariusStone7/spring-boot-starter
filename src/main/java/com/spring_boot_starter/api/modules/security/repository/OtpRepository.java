package com.spring_boot_starter.api.modules.security.repository;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.modules.security.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    public List<Otp> findByUserOrderByCreatedAtDesc(User user);
}
