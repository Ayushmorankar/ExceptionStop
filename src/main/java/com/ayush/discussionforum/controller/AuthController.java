package com.ayush.discussionforum.controller;

import com.ayush.discussionforum.dto.*;
import com.ayush.discussionforum.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "Register student")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest){
        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.status(200).body(authService.login(loginRequest));
    }

    @ApiOperation(value = "Endpoint which user will access to activate account")
    @GetMapping("/activate-account/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token){
        authService.activateAccount(token);
        return ResponseEntity.status(200).body("Account activated");
    }

    @ApiOperation(value = "Get refreshed jwt after they are expired")
    @PostMapping("/refresh-token")
    public AuthenticationResponse getJwtWithRefreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @ApiOperation(value = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest refreshTokenRequest){
        authService.logout(refreshTokenRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
