package com.ayush.discussionforum.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

@Service
public class JwtUtil {

    private KeyStore keyStore;
    @Value("${token.expiration.time.jwt}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/discussionforum.jks");
            keyStore.load(resourceAsStream, "password".toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(500),
                    "Exception occurred while loading keystore");
        }
    }

    private PrivateKey getPrivateKey(){
        try{
            return (PrivateKey) keyStore.getKey("discussionforum", "password".toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(500),
                    "Exception occurred while retrieving private key");
        }
    }

    private PublicKey getPublicKey(){
        try{
            return keyStore.getCertificate("discussionforum").getPublicKey();
        } catch (KeyStoreException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(500),
                    "Exception occurred while retrieving public key");
        }
    }

    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .signWith(getPrivateKey())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public boolean validateToken(String jwt){
        Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }
}
