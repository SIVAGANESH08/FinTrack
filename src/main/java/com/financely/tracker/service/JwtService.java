package com.financely.tracker.service;

import com.mysql.cj.util.TimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    public static final String SECRET = "1138011BC360FE4FD588E3F603FEF91280471DB38447BD472DD30390AAE122AFFE9BDB5A844195C42F8B4FD5E63B6831BA981EAAFAC7CFEBDAA074B5E15B67B9";
    public static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails){
        Map<String,String> claims = new HashMap<>();
        claims.put("iss", "finance-tracker");

        return Jwts.builder()
                    .claims(claims)
                    .subject(userDetails.getUsername())
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                    .signWith(generateKey())
                    .compact();

    }

    private SecretKey generateKey(){
        byte[] decodeKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodeKey);
    }

    private Claims getClaims(String jwt){
        return Jwts.parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    }

    public String extractUsername(String jwt){
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    public boolean isTokenValid(String jwt){
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
