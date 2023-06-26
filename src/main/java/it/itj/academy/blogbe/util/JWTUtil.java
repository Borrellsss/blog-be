package it.itj.academy.blogbe.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JWTUtil {
    @Value("${security.issuer}")
    private String issuer;
    @Value("${security.secret}")
    private String secret;
    @Value("${security.expiration}")
    private int expiration;
    @Value("${security.prefix}")
    private String prefix;

    public String generate(String subject, Map<String, String> privateClaims) {
        DateTime now = new DateTime();
        JWTCreator.Builder jwtBuilder = JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withIssuedAt(now.toDate())
            .withExpiresAt(now.plusMillis(expiration).toDate());
        for (String key : privateClaims.keySet()) {
            jwtBuilder.withClaim(key, privateClaims.get(key));
        }
        return jwtBuilder.sign(Algorithm.HMAC512(secret));
    }
    public DecodedJWT decode(String jwt) {
        return JWT.require(Algorithm.HMAC512(secret))
            .withIssuer(issuer)
            .build()
            .verify(jwt);
    }
    public String getPrefix() {
        return prefix;
    }
}
