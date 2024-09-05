package com.example.jwttokendemo.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private static final String
            SECRET_KEY = "C3869EEFBF651BA1B7774C822F42DB646501518076253C3CC694CD4C8998EC8F01BC437F23E3CEE9D3333B4D61CDF083AE5E71009A56BAC5057D9B694BC04677";
    private static final long DURATION = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        String jwt = Jwts.builder()
                .issuer("Sprin-Security-Api")
                .subject("Jwt-token")
                .claim("username", userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(DURATION)))
                .signWith(generateKey())
                .compact();

        System.err.printf("\n MY_JWT_TOKEN \n [%s]\n", jwt);
        return jwt;
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        return claims.get("username", String.class);
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        Date expDate = claims.getExpiration();
        return expDate.after(Date.from(Instant.now()));
    }
}
