package com.ayush.discussionforum.service;

import com.ayush.discussionforum.dto.RefreshTokenRequest;
import com.ayush.discussionforum.model.RefreshToken;
import com.ayush.discussionforum.model.Student;
import com.ayush.discussionforum.repository.RefreshTokenRepository;
import com.ayush.discussionforum.repository.StudentRepository;
import com.ayush.discussionforum.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final StudentRepository studentRepository;
    private final Utils utils;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(utils.generateRefreshTokenString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(401), "Invalid token"));
    }

    public void deleteRefreshToken(RefreshTokenRequest refreshTokenRequest){
        Student student = studentRepository.findByUsername(refreshTokenRequest.getUsername())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.valueOf(404), "Username not found"));
        Byte activeSessions = student.getActiveSessions();
        student.setActiveSessions(--activeSessions);
        refreshTokenRepository.deleteByToken(refreshTokenRequest.getRefreshToken());
    }
}
