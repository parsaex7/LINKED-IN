package Server.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class JwtController {
    private static final Key key = Keys.hmacShaKeyFor("p13842005".getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    public static String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, algorithm)
                .compact();
    }

    public static String verifyToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public static boolean isTokenValid(String token) {
        return verifyToken(token) != null;
    }
}