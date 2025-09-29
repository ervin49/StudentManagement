package com.example.usermanagement.services;

import com.example.usermanagement.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    public String generateToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractClaim(token, Claims::getSubject);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public Key getSignInKey() {
        String jwtSecretKey = "1982c65d2592ba48322bd8b605f03d76fda133fc64e66a7cda1b353633b5bc33";
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
        Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isJwt(String jwt) {
        String[] jwtSplitted = jwt.split("\\.");
        if (jwtSplitted.length != 3)
            return false;
        try {
            String jsonFirstPart = new String(Base64.getDecoder().decode(jwtSplitted[0]));
            JSONObject firstPart = new JSONObject(jsonFirstPart);
            if (!firstPart.has("alg"))
                return false;
            String jsonSecondPart = new String(Base64.getDecoder().decode(jwtSplitted[1]));
            JSONObject secondPart = new JSONObject(jsonSecondPart);
        } catch (JSONException err) {
            return false;
        }
        return true;
    }

    public Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
