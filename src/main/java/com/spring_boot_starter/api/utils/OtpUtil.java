package com.spring_boot_starter.api.utils;

import com.spring_boot_starter.api.exceptions.OtpException;
import com.spring_boot_starter.api.modules.security.entity.Otp;
import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.modules.security.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class OtpUtil {

    private final static int MAX_ATTEMPTS = 3;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.otp.expiration}")
    private int jwtOtpExpiration;

    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }


    public String createOtp(User user) {
        String otp = generateOtp();

        Otp otpEntity = new Otp();
        otpEntity.setUser(user);
        otpEntity.setOtp_hash(passwordEncoder.encode(otp));
        otpEntity.setExpiresAt(Instant.now().plusMillis(this.jwtOtpExpiration));
        otpEntity.setCreatedAt(Instant.now());

        otpRepository.save(otpEntity);

        return otp;
    }


    public void verifyOtp(User user, String otp) {

        Otp otpEntity = otpRepository.findByUserOrderByCreatedAtDesc(user).getFirst();

        if (otpEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new OtpException("OTP is expired: OTP expired at " + otpEntity.getExpiresAt().toString());
        }

        if (otpEntity.getAttempts() >= MAX_ATTEMPTS) {
            throw new OtpException("Too many attempts, retry with new OTP");
        }

        otpEntity.incrementAttempt();

        if (!passwordEncoder.matches(otp, otpEntity.getOtp_hash())) {
            otpRepository.save(otpEntity);
            throw new OtpException("Invalid OTP");
        }

        otpEntity.setUsed(true);
        otpRepository.save(otpEntity);
    }


    public void sendOtp(String to, String otp) {
        emailUtil.sendOtp(to, otp);
    }
}
