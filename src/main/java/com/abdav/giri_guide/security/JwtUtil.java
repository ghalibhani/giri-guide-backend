package com.abdav.giri_guide.security;

import com.abdav.giri_guide.entity.AppUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
    @Value("${app.giri-guide.jwt-secret}")
    private String jwtSecret;

    @Value("${app.giri-guide.app-name}")
    private String appName;

    @Value("${app.giri-guide.jwt-expiration}")
    private Long expiryTimeInSeconds;

    public String generateToken(AppUser appUser){
        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(appUser.getId())
                    .withExpiresAt(Instant.now().plusSeconds(expiryTimeInSeconds))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", appUser.getRole().name())
                    .sign(algorithm);
        } catch (JWTCreationException e){
            log.error("Error Generating token : {}",e.getMessage());
            throw new RuntimeException("JWT generation failed", e);
        }
    }

    public boolean verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        } catch (JWTVerificationException e){
            log.error("Error Verifying token : {}",e.getMessage());
            return false;
        }
    }

    public Map<String, String> getUserInfoByToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String,String> userInfo = new HashMap<>();
            userInfo.put("UserId", decodedJWT.getSubject());
            userInfo.put("role", decodedJWT.getClaim("role").asString());

            return userInfo;
        }catch (JWTVerificationException e){
            log.error("Error Verifying token : {}",e.getMessage());
            return null;
        }
    }
}
