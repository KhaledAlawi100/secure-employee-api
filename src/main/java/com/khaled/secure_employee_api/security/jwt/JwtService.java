package com.khaled.secure_employee_api.security.jwt;

import com.khaled.secure_employee_api.common.exception.ExpiredJwtTokenException;
import com.khaled.secure_employee_api.common.exception.InvalidJwtException;
import com.khaled.secure_employee_api.security.user.CustomUserDetails;
import com.khaled.secure_employee_api.user.entity.AppUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails) {

        return generateToken(
                Collections.emptyMap(),
                userDetails
        );
    }

    public String generateAccessToken(AppUser appUser) {

        return generateToken(
                new CustomUserDetails(appUser)
        );
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {

        return createToken(extraClaims, userDetails);
    }

    private String createToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {

        return Jwts.builder()
                .claims(buildClaims(extraClaims))
                .subject(getSubject(userDetails))
                .issuer(getIssuer())
                .issuedAt(getCurrentDate())
                .expiration(getExpirationDate())
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        Claims claims = extractAllClaims(token);

        String username = claims.getSubject();

        return username.equals(userDetails.getUsername())
                && !isExpired(claims);
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isExpired(Claims claims) {

        return claims.getExpiration().before(new Date());
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public long getAccessTokenExpirationInSeconds() {

        return getAccessTokenExpiration() / 1000;
    }

    public Instant getAccessTokenExpiresAt() {

        return Instant.now()
                .plusMillis(getAccessTokenExpiration());
    }

    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {

        try {

            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException ex) {

            throw new ExpiredJwtTokenException();

        } catch (
                SignatureException |
                MalformedJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex
        ) {

            throw new InvalidJwtException();
        }
    }

    private Date getCurrentDate() {

        return new Date();
    }

    private Date getExpirationDate() {

        return new Date(
                System.currentTimeMillis()
                        + getAccessTokenExpiration()
        );
    }

    private String getSubject(UserDetails userDetails) {

        return userDetails.getUsername();
    }

    private Map<String, Object> buildClaims(
            Map<String, Object> extraClaims
    ) {

        Map<String, Object> claims = new HashMap<>();

        claims.putAll(extraClaims);

        return claims;
    }

    private String getIssuer() {

        return jwtProperties.getIssuer();
    }

    private String getSecret() {

        return jwtProperties.getSecret();
    }

    private long getAccessTokenExpiration() {

        return jwtProperties.getAccessTokenExpiration();
    }
}