package com.ayush.discussionforum.service;

import com.ayush.discussionforum.config.AppConfig;
import com.ayush.discussionforum.dto.LoginRequest;
import com.ayush.discussionforum.dto.AuthenticationResponse;
import com.ayush.discussionforum.dto.RefreshTokenRequest;
import com.ayush.discussionforum.dto.RegisterRequest;
import com.ayush.discussionforum.model.NotificationEmail;
import com.ayush.discussionforum.model.Student;
import com.ayush.discussionforum.model.VerificationToken;
import com.ayush.discussionforum.repository.StudentRepository;
import com.ayush.discussionforum.repository.VerificationTokenRepository;
import com.ayush.discussionforum.utils.JwtUtil;
import com.ayush.discussionforum.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final Utils utils;
    private final AppConfig appConfig;


    @Transactional
    public void register(RegisterRequest registerRequest) {

        if(studentRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Username is taken");
        }

        else if(studentRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.valueOf(409), "Email already present");
        }

        Student newStudent = Student.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdDate(Instant.now())
                .email(registerRequest.getEmail())
                .enabled(false)
                .cfUsername(registerRequest.getCfUsername())
                .agreedToTerms(registerRequest.isAgreedToTerms())
                .publicStudentId(utils.generatePublicStudentId(15))
                .activeSessions(Byte.parseByte("0"))
                .build();

        studentRepository.save(newStudent);

        String verificationToken = generateVerificationToken(newStudent);

        mailService.sendMail(new NotificationEmail("Please activate your account",
                newStudent.getEmail(),
                appConfig.getAppUrl()+"/v1/api/auth/activate-account/"+verificationToken));
    }

    private String generateVerificationToken(Student student){

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setStudent(student);
        verificationToken.setExpiryDate(Instant.now().plusSeconds(604800L));

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Transactional
    public void activateAccount(String token){

        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);

        if(verificationTokenOptional.isPresent()){
            VerificationToken verificationToken = verificationTokenOptional.get();
            if(Instant.now().compareTo(verificationToken.getExpiryDate())>0){
                throw new ResponseStatusException(HttpStatus.valueOf(401), "Invalid token");
            }
            Student student = verificationToken.getStudent();
            student.setEnabled(true);
            studentRepository.save(student);
            verificationTokenRepository.delete(verificationToken);
        }
        else throw new ResponseStatusException(HttpStatus.valueOf(401), "Invalid token");
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Student currentStudent = getCurrentStudent();
        Byte activeSessions = currentStudent.getActiveSessions();
        if(activeSessions<3) {

            currentStudent.setActiveSessions(++activeSessions);
            String jwt = jwtUtil.generateToken(authentication);

            return AuthenticationResponse.builder()
                    .jwt(jwt)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(1800000L))
                    .build();
        }
        else throw new ResponseStatusException(HttpStatus.valueOf(503),
                "Only 3 devices are allowed to be logged in at a time");
    }

    @Transactional(readOnly = true)
    public Student getCurrentStudent(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return studentRepository.findByUsername(user.getUsername())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.valueOf(404), "Username not found"));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String jwt = jwtUtil.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(1800000L))
                .build();
    }

    public void logout(RefreshTokenRequest refreshTokenRequest){
        String refreshToken = refreshTokenRequest.getRefreshToken();
        refreshTokenService.validateRefreshToken(refreshToken);
        refreshTokenService.deleteRefreshToken(refreshTokenRequest);
    }
}
