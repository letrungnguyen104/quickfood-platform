package com.quickfood.authservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.quickfood.authservice.dto.request.ForgotPasswordRequest;
import com.quickfood.authservice.dto.request.GoogleLoginRequest;
import com.quickfood.authservice.dto.request.LoginRequest;
import com.quickfood.authservice.dto.request.RefreshTokenRequest;
import com.quickfood.authservice.dto.request.RegisterRequest;
import com.quickfood.authservice.dto.request.ResendOtpRequest;
import com.quickfood.authservice.dto.request.ResetPasswordRequest;
import com.quickfood.authservice.dto.request.VerifyOtpRequest;
import com.quickfood.authservice.dto.response.AuthResponse;
import com.quickfood.authservice.entity.User;
import com.quickfood.authservice.enums.AuthProvider;
import com.quickfood.authservice.enums.Role;
import com.quickfood.authservice.exception.BusinessException;
import com.quickfood.authservice.exception.ErrorCode;
import com.quickfood.authservice.repository.UserRepository;
import com.quickfood.authservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

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
                .role(Role.ROLE_CUSTOMER)
                .provider(AuthProvider.LOCAL)
                .isVerified(false)
                .status("ACTIVE")
                .build();

        userRepository.save(user);
        otpService.generateAndSendOtp(request.getEmail());
    }

    @Transactional
    public void resendOtp(ResendOtpRequest request) {
        boolean exists = userRepository.existsByEmail(request.getEmail());
        if (!exists) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
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
        String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .user(AuthResponse.UserInfo.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .avatarUrl(user.getAvatarUrl())
                    .role(user.getRole().name())
                    .build())
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

        String newAccessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        String newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional
    public AuthResponse googleLogin(GoogleLoginRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new BusinessException(ErrorCode.INVALIDE_GOOGLE_TOKEN);
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String subjectId = payload.getSubject();

            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                String baseUsername = email.split("@")[0];
                String username = baseUsername;
                int suffix = 1;
                while (userRepository.existsByUsername(username)) {
                    username = baseUsername + suffix++;
                }

                user = User.builder()
                        .username(username)
                        .email(email)
                        .fullName(name)
                        .avatarUrl(pictureUrl)
                        .role(Role.ROLE_CUSTOMER)
                        .provider(AuthProvider.GOOGLE)
                        .providerId(subjectId)
                        .isVerified(true)
                        .status("ACTIVE")
                        .build();
                userRepository.save(user);
            } else if (user.getProvider() != AuthProvider.GOOGLE) {
                throw new BusinessException(ErrorCode.EMAIL_EXISTED, "This email is already registered. Please login with your password.");
            }

            String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
            String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

            return AuthResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .user(AuthResponse.UserInfo.builder()
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .avatarUrl(user.getAvatarUrl())
                        .role(user.getRole().name())
                        .build())
                    .build();

        } catch (BusinessException e) {
            throw e; 
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Failed to authenticate with Google");
        }
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        otpService.generateAndSendOtp(user.getEmail());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        otpService.validateOtp(request.getEmail(), request.getOtp());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void upgradeUserToVendor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        user.setRole(Role.ROLE_VENDOR);
        userRepository.save(user);
    }

}