package com.quickfood.authservice.service;

import com.quickfood.authservice.exception.BusinessException;
import com.quickfood.authservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    
    private static final String OTP_PREFIX = "OTP_";
    private static final long OTP_EXPIRATION_MINUTES = 5;

    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(
                OTP_PREFIX + email, 
                otp, 
                Duration.ofMinutes(OTP_EXPIRATION_MINUTES)
        );
        
        sendEmail(email, otp);
    }

    public void validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + email);
        
        if (storedOtp == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "OTP has expired or does not exist");
        }
        if (!storedOtp.equals(otp)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Invalid OTP");
        }
        
        redisTemplate.delete(OTP_PREFIX + email);
    }

    private void sendEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("QuickFood - Verify your account");
            message.setText("Welcome to QuickFood! Your verification code is: " + otp + 
                            "\nThis code will expire in 5 minutes.");
            
            mailSender.send(message);
            log.info("OTP sent to email: {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new BusinessException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Failed to send verification email");
        }
    }
}