package com.kshop.backend.service;

import com.kshop.backend.dto.*;
import com.kshop.backend.entity.User;
import com.kshop.backend.entity.UserRole;
import com.kshop.backend.repository.UserRepository;
import com.kshop.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    // 회원가입
    @Transactional
    public UserResponse signup(SignupRequest request) {
        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다");
        }
        
        // 2. User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(UserRole.USER)
                .build();
        
        // 3. DB에 저장
        User savedUser = userRepository.save(user);
        
        // 4. Entity를 DTO로 변환하여 반환
        return convertToUserResponse(savedUser);
    }
    
    // 로그인
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다"));
        
        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다");
        }
        
        // 3. JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());
        
        // 4. 응답 생성
        UserResponse userResponse = convertToUserResponse(user);
        return new JwtResponse(token, userResponse);
    }
    
    // Entity를 DTO로 변환하는 헬퍼 메서드
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
