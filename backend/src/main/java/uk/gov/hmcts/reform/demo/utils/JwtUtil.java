package uk.gov.hmcts.reform.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "s3cR3tK3y!@#2024Ex@mpleWithL0ngString"; // Use a secure key

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                .build()
                .verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT != null ? decodedJWT.getSubject() : null;
    }
}
