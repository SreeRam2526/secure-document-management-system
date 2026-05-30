package com.sreeram.documentmanagementsystem.service;

import com.sreeram.documentmanagementsystem.dto.LoginRequest;
import com.sreeram.documentmanagementsystem.dto.RegisterRequest;
import com.sreeram.documentmanagementsystem.entity.Role;
import com.sreeram.documentmanagementsystem.entity.User;
import com.sreeram.documentmanagementsystem.repository.UserRepository;
import com.sreeram.documentmanagementsystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sreeram.documentmanagementsystem.dto.ApiResponse;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final Logger logger =
            LoggerFactory.getLogger(AuthService.class);

    public ApiResponse register(RegisterRequest request){

        logger.info(
                "Register request received for email: {}",
                request.getEmail()
        );

        if (userRepository.findByEmail(
                request.getEmail()
        ).isPresent()) {

            logger.error(
                    "Registration failed. Email already exists: {}",
                    request.getEmail()
            );

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.USER)
                .build();

        userRepository.save(user);

        logger.info(
                "User registered successfully: {}",
                request.getEmail()
        );

        return ApiResponse.builder()
                .success(true)
                .message("User Registered Successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponse login(LoginRequest request) {

        logger.info(
                "Login request received for email: {}",
                request.getEmail()
        );

        User user = userRepository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() -> {

                    logger.error(
                            "Login failed. User not found: {}",
                            request.getEmail()
                    );

                    return new RuntimeException(
                            "User Not Found"
                    );
                });

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {

            logger.error(
                    "Login failed. Invalid password for email: {}",
                    request.getEmail()
            );

            throw new RuntimeException(
                    "Invalid Password"
            );
        }

        logger.info(
                "JWT token generated for user: {}",
                request.getEmail()
        );

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        return ApiResponse.builder()
                .success(true)
                .message(token)
                .timestamp(LocalDateTime.now())
                .build();
    }
}