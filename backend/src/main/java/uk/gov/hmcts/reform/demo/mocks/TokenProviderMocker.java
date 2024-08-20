package uk.gov.hmcts.reform.demo.mocks;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenProviderMocker {

    private static final String SECRET = "s3cR3tK3y!@#2024Ex@mpleWithL0ngString";
    private static final long EXPIRATION_TIME = 864_000_00;  // 1 den

    // Issue a JWT token with the subject (e.g., username)
    public String provideToken(String subject) {
        return JWT.create()
            .withSubject(subject)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(Algorithm.HMAC256(SECRET));
    }

    // Verify and decode the JWT token
    public String verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))
                .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject(); // This returns the subject (e.g., username)
        } catch (JWTVerificationException exception) {
            // Invalid token
            return null;
        }
    }
}
