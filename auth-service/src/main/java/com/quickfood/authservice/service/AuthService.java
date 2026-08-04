package com.quickfood.authservice.service;

import com.quickfood.authservice.dto.request.LoginRequest;
import com.quickfood.authservice.dto.request.RefreshTokenRequest;
import com.quickfood.authservice.dto.request.RegisterRequest;
import com.quickfood.authservice.dto.request.VerifyOtpRequest;
import com.quickfood.authservice.dto.response.AuthResponse;
import com.quickfood.authservice.entity.User;
import com.quickfood.authservice.enums.AuthProvider;
import com.quickfood.authservice.exception.BusinessException;
import com.quickfood.authservice.exception.ErrorCode;
import com.quickfood.authservice.repository.UserRepository;
import com.quickfood.authservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTED);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("CUSTOMER")
                .provider(AuthProvider.LOCAL)
                .isVerified(false)
                .status("ACTIVE")
                .build();

        userRepository.save(user);
        otpService.generateAndSendOtp(request.getEmail());
    }

    @Transactional
    public void verifyOtp(VerifyOtpRequest request) {
        otpService.validateOtp(request.getEmail(), request.getOtp());
        User user = userRepository.findByUsernameOrEmail(request.getEmail(), request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.isVerified()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Account is already verified");
        }
        user.setVerified(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getIdentifier();
        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (user.getPassword() == null || user.getProvider() != AuthProvider.LOCAL) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, "Please login with your social account");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String username = refreshTokenService.getUsernameFromRefreshToken(request.getRefreshToken());
        
        if (username == null) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());

        String newAccessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
        String newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}