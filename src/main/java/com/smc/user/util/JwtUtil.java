package com.smc.user.util;

import com.smc.user.dto.AuthUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * generate jwt token
     * @param user user details
     * @return jwtToken
     */
    public String generateToken(AuthUserDto user) {
        Set<String> roles = user.getRoles().stream().map(roleDto -> roleDto.getName()).collect(Collectors.toSet());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", roles);

        return Jwts.builder().setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getValidity() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * get all claims from token, print the ExpiredJwtException
     * @param token
     * @return claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getSecret())
                            .parseClaimsJws(token)
                            .getBody();
        } catch (Exception ex) {
            return null;
        }
    }



    /**
     * validate if the token is expired, if expiration is null, return true
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * validate token
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);

//        return ((StringUtils.equals(username, userDetails.getUsername())) &&
//                (!isTokenExpired(token)));
        return (userDetails.getUsername().equals(username) &&
                (!isTokenExpired(token)));
    }


    private Date getExpirationFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    private String getUsernameFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
