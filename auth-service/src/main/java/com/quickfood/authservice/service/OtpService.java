package com.quickfood.authservice.service;

import com.quickfood.authservice.exception.BusinessException;
import com.quickfood.authservice.exception.ErrorCode;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        
        sendHtmlEmail(email, otp);
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

    private void sendHtmlEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("QuickFood - Your Verification Code");
            String htmlContent = """
                <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9fafb; border-radius: 10px;">
                    <div style="text-align: center; padding-bottom: 20px;">
                        <h1 style="color: #ea580c; margin: 0; font-size: 28px;">QuickFood</h1>
                        <p style="color: #6b7280; margin-top: 5px; font-size: 16px;">Food Delivery Reimagined</p>
                    </div>
                    <div style="background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);">
                        <h2 style="color: #1f2937; margin-top: 0;">Verify your email address</h2>
                        <p style="color: #4b5563; font-size: 16px; line-height: 1.5;">Hello,</p>
                        <p style="color: #4b5563; font-size: 16px; line-height: 1.5;">Please use the following 6-digit verification code to complete your request. This code is valid for <strong>5 minutes</strong>.</p>
                        
                        <div style="text-align: center; margin: 30px 0;">
                            <span style="display: inline-block; background-color: #fff7ed; border: 2px dashed #f97316; color: #ea580c; font-size: 32px; font-weight: bold; letter-spacing: 5px; padding: 15px 30px; border-radius: 8px;">
                                %s
                            </span>
                        </div>
                        
                        <p style="color: #4b5563; font-size: 14px; line-height: 1.5;">If you didn't request this code, you can safely ignore this email.</p>
                    </div>
                    <div style="text-align: center; padding-top: 20px; color: #9ca3af; font-size: 12px;">
                        &copy; 2026 QuickFood Platform. All rights reserved.
                    </div>
                </div>
                """.formatted(otp);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("HTML OTP sent to email: {}", to);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new BusinessException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Failed to send verification email");
        }
    }
}