package org.global.housing.config.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final byte[] secretBytes;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret,
                            @Value("${app.jwt.expiration-ms}") long validityInMilliseconds) {
        this.secretBytes = secret.getBytes();
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String subject, String role) {
        Date now = Date.from(Instant.now());
        Date expiry = Date.from(Instant.now().plusMillis(validityInMilliseconds));

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("role", role)
                .issueTime(now)
                .expirationTime(expiry)
                .build();

        try {
            JWSSigner signer = new MACSigner(secretBytes);
            SignedJWT signedJWT = new SignedJWT(new com.nimbusds.jose.JWSHeader(JWSAlgorithm.HS256), claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretBytes);
            if (!signedJWT.verify(verifier)) return false;
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            return exp == null || exp.after(new Date());
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    public JWTClaimsSet getClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }
}
