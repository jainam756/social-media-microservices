package com.example.authservice.service;

import com.example.authservice.config.JwtConfig;
import com.example.authservice.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@AllArgsConstructor
public class TokenService {
    private final JwtConfig jwtConfig;
    public String generateToken(Authentication authentication){
        //header+payloads+sign
        var header=new JWSHeader.Builder(jwtConfig.getAlgorithm())
                .type(JOSEObjectType.JWT)
                .build();
        var roles=authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        var builder=new JWTClaimsSet.Builder().issuer("jainam")
                .issueTime(Date.from(Instant.now()))
                .subject(authentication.getName())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)));
        var user=(User)authentication.getPrincipal();
        builder.claim("roles",roles);
        builder.claim("name",user.getName());
        builder.claim("email",user.getEmail());
        builder.claim("id",user.getId());
        var key=jwtConfig.getSecretKey();
        var jwt=new SignedJWT(header,builder.build());
        try {
            var signer = new MACSigner(key);
            jwt.sign(signer);
        }catch (JOSEException e) {
            throw new RuntimeException("Error generating jwt",e);
        }
        return jwt.serialize();
    }
}
