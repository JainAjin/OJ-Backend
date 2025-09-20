package com.OJ.OnlineJudge.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private final String SECRET = "nayeonjeongyeonmomosanajihyominadahyunchaeyoungtzuyu";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username) {
         return Jwts.builder()
                 .setSubject(username)
                 .setIssuedAt(new Date())
                 .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7))
                 .signWith(key, SignatureAlgorithm.HS256)
         .compact();
    }
    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isExpired(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, String username){
        return username.equals(extractUsername(token))&& !isExpired(token);
    }
}
